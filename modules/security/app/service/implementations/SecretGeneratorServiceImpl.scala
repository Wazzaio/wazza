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

package service.security.implementations

import play.api.libs.Crypto
import service.security.definitions._
import java.security.SecureRandom
import java.math.BigInteger
import SecretGeneratorServiceContext._
import java.util.UUID

class SecretGeneratorServiceImpl extends SecretGeneratorService {

  private final val NumberBits = 130
  private final val Radix = 32

  def generateSecret(secretType: Int): String = {
    secretType match {
      case Id => {
        val random = new SecureRandom()
        new BigInteger(NumberBits, random).toString(Radix)
      }
      case ApiKey => Crypto.generateToken
      case _ => null
    }
  }
}
