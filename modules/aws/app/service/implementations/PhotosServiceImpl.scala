/*
 * Wazza
 * https://github.com/Wazzaio/wazza
 * Copyright (C) 2013-2015  Duarte Barbosa, João Vazão Vasques
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package service.aws.implementations

import service.aws.definitions._
import scala.util.{Try, Success, Failure}
import play.api.Play
import com.amazonaws.auth._
import com.amazonaws.services.s3._
import com.amazonaws.services.s3.model._
import com.amazonaws.AmazonServiceException
import com.amazonaws.HttpMethod
import scala.concurrent._
import ExecutionContext.Implicits.global
import play.api.mvc.MultipartFormData._
import java.io.File
import models.aws._
import play.api.http.Status
import java.io.FileInputStream
import java.security.MessageDigest
import javax.xml.bind.annotation.adapters.HexBinaryAdapter
import java.time._
import java.util.Date

class PhotosServiceImpl extends PhotosService {

  private def generateFileName(file: File): String = {
    (new HexBinaryAdapter()).marshal(MessageDigest.getInstance("SHA-1").digest(file.getName.getBytes()))
  }

  private def generateS3ObjectURL(bucketName: String, fileName: String, s3Client: AmazonS3Client): String = {
    val expirationDate = LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault())
      .withYear(ExpirationDate.Year)
      .withMonth(ExpirationDate.Month)
      .withDayOfMonth(ExpirationDate.Day)

    val request = new GeneratePresignedUrlRequest(bucketName, fileName, HttpMethod.GET)
    request.setExpiration(Date.from(expirationDate.atZone(ZoneId.systemDefault()).toInstant()))
    s3Client.generatePresignedUrl(request).toString
  }

  def upload(file: File): Future[UploadPhotoResult] = {
    val promise = Promise[UploadPhotoResult]

    Future {
      PhotosBucket match {
        case Success(bucket) => {
          try {
              val s3Client = getS3Client(bucket).get
              val fileName = generateFileName(file)
              val request = new PutObjectRequest(bucket,fileName, new FileInputStream(file), new ObjectMetadata())
              request.withCannedAcl(CannedAccessControlList.PublicRead)
              s3Client.putObject(request)
              promise.success(new UploadPhotoResult(fileName, generateS3ObjectURL(bucket, fileName, s3Client)))
            } catch {
              case err: AmazonServiceException if err.getStatusCode == Status.NOT_FOUND => promise.failure(new S3NotFound(bucket, file.getName))
              case err: Throwable => promise.failure(new S3Failed(err))
            }
        }
        case Failure(failure) => promise.failure(failure)
      }
    }

    promise.future
  }

  def delete(imageName: String): Future[Unit] = {
    val promise = Promise[Unit]

    Future {
      PhotosBucket match {
        case Success(bucket) => {
          try {
            val s3Client = getS3Client(bucket).get
            val request = new DeleteObjectRequest(bucket, imageName)
            s3Client.deleteObject(request)
            promise.success()
          } catch {
              case err: AmazonServiceException if err.getStatusCode == Status.NOT_FOUND => promise.failure(new S3NotFound(bucket, imageName))
              case err: Throwable => promise.failure(new S3Failed(err))
          }
        }
        case Failure(failure) => promise.failure(failure)
      }
    }

    promise.future
  }
}
