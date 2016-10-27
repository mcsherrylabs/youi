package io.youi

import java.io.File
import java.net.URL

import scala.language.implicitConversions

package object http {
  implicit def string2Content(value: String): Content = Content.string(value)
  implicit def file2Content(file: File): Content = Content.file(file)
  implicit def url2Content(url: URL): Content = Content.url(url)
}