package io.youi.component

import io.youi.component.extras.{ComponentPosition, ComponentSize}
import io.youi.event.{EventSupport, Events, HTMLEvents}
import io.youi.style.Visibility
import io.youi.{MapStore, Store, Unique, Updatable}
import io.youi.task.TaskSupport
import io.youi.theme.{ComponentTheme, Theme}
import reactify._

import scala.annotation.tailrec
import scala.concurrent.duration._

/**
  * Component represents the root type for all on-screen elements. This includes both HTML and Canvas.
  */
trait Component extends TaskSupport with ComponentTheme {
  private var _initialized: Boolean = false

  lazy val store: Store = new MapStore

  private lazy val transform = rateLimited(1.millis)(updateTransform())
  private lazy val rendering = rateLimited(1.millis)(updateRendering())

  override protected def invalidateTransform(): Unit = transform.flag()

  override protected def invalidateRendering(): Unit = rendering.flag()

  override def updateTasks(): Boolean = visible()

  /**
    * Position information for placement of this component on the screen.
    */
  def position: ComponentPosition

  /**
    * Size information for determining the dimensions of this component.
    */
  def size: ComponentSize

  /**
    * Generated unique identifier for this element.
    */
  lazy val id: Var[String] = Var(s"$componentType.${Unique(length = 4, characters = Unique.Readable).toLowerCase}")

  /**
    * Parent to this element.
    */
  lazy val parent: Var[Option[Component]] = Var(None)

  lazy val root: Var[Option[Component]] = Var(parent().flatMap(_.root()))

  val visible: Val[Boolean] = Val(visibility() == Visibility.Visible && parent().exists(_.visible()))

  /**
    * List of `Updatable` instances derived from the `updatables` method.
    */
  private lazy val internalUpdatables: Val[List[Updatable]] = Val(updatables)

  invalidateTransform()

  override protected def defaultParentTheme: Theme = Component

  /**
    * Events functionality for monitoring and even firing events on this component.
    */
  def event: EventSupport

  /**
    * The type of Component. This is useful for client-side introspection and logging. Each custom Component instance
    * should represent a unique `type`.
    */
  def componentType: String

  /**
    * True if this Component's `init` method has been invoked.
    */
  def initialized: Boolean = _initialized

  /**
    * List of `Updatable` instances this Component represents.
    */
  protected def updatables: List[Updatable] = Nil

  /**
    * Called automatically the first time this Component is connected to the document.
    */
  protected def init(): Unit = {}

  override def update(delta: Double): Unit = {
    // Make sure we initialize before we do anything else
    if (!initialized) {
      init()
      _initialized = true
    }

    super.update(delta)

    updateUpdatables(delta, internalUpdatables())
  }

  override protected def updateTransform(): Unit = {
    super.updateTransform()

    size.measured.width := measuredWidth
    size.measured.height := measuredHeight
  }

  @tailrec
  private def updateUpdatables(delta: Double, updatables: List[Updatable]): Unit = if (updatables.nonEmpty) {
    updatables.head.update(delta)
    updateUpdatables(delta, updatables.tail)
  }

  protected def childComponents: Vector[Component] = Vector.empty

  object sibling {
    def previous(): Option[Component] = parent().flatMap { p =>
      val children = p.childComponents
      val index = children.indexOf(Component.this)
      if (index > 0) {
        Some(children(index - 1))
      } else {
        None
      }
    }

    def next(): Option[Component] = parent().flatMap { p =>
      val children = p.childComponents
      val index = children.indexOf(Component.this)
      if (index < children.size - 1) {
        Some(children(index + 1))
      } else {
        None
      }
    }
  }

  protected def measuredWidth: Double
  protected def measuredHeight: Double

  override def toString: String = id()
}

object Component extends ComponentTheme {
  override protected def defaultParentTheme: Theme = Theme

  def childrenFor(component: Component): Vector[Component] = component.childComponents

  object measured {
    def width(component: Component): Double = component.measuredWidth
    def height(component: Component): Double = component.measuredHeight
  }
}