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

package persistence.utils

import org.joda.time.{DateTime, LocalDate, Days}
import scala.collection.immutable.StringOps
import java.text.SimpleDateFormat
import java.util.Date
import scala.collection.immutable.StringOps
import org.joda.time.format.DateTimeFormat

object DateUtils {

  private val Format = "yyyy-MM-dd HH:mm:ss Z"
  private val dateFormat = new SimpleDateFormat(Format)

  def buildDateFromString(dateStr: String): Date = {
    dateFormat.parse(dateStr)
  }

  def buildJodaDateFromString(dateStr: String): DateTime = {
    DateTimeFormat.forPattern(Format).parseDateTime(dateStr)
  }

  def getDateFromString(dateStr: String): Date = {
    val ops = new StringOps(dateStr)
    new SimpleDateFormat("yyyy-MM-dd").parse(ops.take(ops.indexOf('T')))
  }

  def getNumberDaysBetweenDates(d1: Date, d2: Date): Int = {
    Days.daysBetween(new LocalDate(d1), new LocalDate(d2)).getDays()
  }

   def getNumberSecondsBetweenDates(d1: Date, d2: Date): Float = {
    (new LocalDate(d2).toDateTimeAtCurrentTime.getMillis - new LocalDate(d1).toDateTimeAtCurrentTime().getMillis) / 1000
  }

}
