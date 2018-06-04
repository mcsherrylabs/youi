package io.youi.example.ui

import io.youi.component.DrawableComponent
import io.youi.example.screen.UIExampleScreen
import io.youi.image.Image

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class DrawableExample extends UIExampleScreen {
  override def title: String = "Drawable Example"
  override def path: String = "/examples/drawable.html"

  override def createUI(): Future[Unit] = Image("/images/cuteness.jpg").map { image =>
    val component = new DrawableComponent {
      size.width := image.width
      size.height := image.height
      position.center := container.size.center
      position.middle := container.size.middle

      drawable := image
    }
    container.children += component
  }
}