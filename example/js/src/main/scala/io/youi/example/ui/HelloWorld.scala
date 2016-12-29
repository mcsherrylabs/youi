package io.youi.example.ui

import com.outr.props._
import com.outr.scribe.Logging
import io.youi.UI
import io.youi.html.Button

import scala.scalajs.js.annotation.JSExportTopLevel

object HelloWorld extends Logging {
  import UI._

  title := "Hello World"

  children += new Button {
    text := "Say 'Hello World'"
    position.center := UI.position.center
    position.middle := UI.position.middle

    click.attach { evt =>
      logger.info("Hello, World!")
    }
  }

  @JSExportTopLevel("helloWorld")
  def main(): Unit = {
    init()
  }
}
