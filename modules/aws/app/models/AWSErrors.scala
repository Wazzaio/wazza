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

package models.aws

/** Thrown when an S3 request fails */
class S3Failed (
  message: String,
  cause: Throwable
) extends Exception(message,cause ) {
    def this (cause: Throwable) = this(null, cause)
    def this (message: String) = this(message, null)
}

/** Thrown when an S3 resource is not found */
class S3NotFound (
   bucket: String,
   key: String
) extends S3Failed (
    "S3 resource not found: %s/%s".format(bucket, key)
)
