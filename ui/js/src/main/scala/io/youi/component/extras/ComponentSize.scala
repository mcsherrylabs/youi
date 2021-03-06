package io.youi.component.extras

import io.youi.component.Component
import io.youi.spatial.Size
import reactify.{Val, Var}

trait ComponentSize {
  protected def component: Component

  def apply(): Size = Size(width(), height())
  def :=(size: => Size): Unit = {
    width := size.width
    height := size.height
  }

  object measured {
    lazy val width: Var[Double] = Var(0.0)
    lazy val height: Var[Double] = Var(0.0)
  }

  lazy val width: Var[Double] = Var(0.0)
  lazy val height: Var[Double] = Var(0.0)

  lazy val center: Val[Double] = Val(width / 2.0)
  lazy val middle: Val[Double] = Val(height / 2.0)

  reset()

  def reset(width: Boolean = true, height: Boolean = true): Unit = {
    if (width) this.width.set(measured.width())
    if (height) this.height.set(measured.height())
  }
}