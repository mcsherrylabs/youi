package io.youi.component.bootstrap

import io.youi.component.{Component, Container, HTMLContainer}
import io.youi.dom
import io.youi.theme.Theme
import org.scalajs.dom.html

class Accordion(element: html.Element) extends HTMLContainer[AccordionEntry](element) with BootstrapComponent[html.Element] {
  def this() = {
    this(dom.create[html.Div]("div"))
  }

  override protected def defaultParentTheme: Theme = Container
  override def componentType: String = "bootstrap.Accordion"

  override protected def init(): Unit = {
    super.init()

    element.classList.add("accordion")
  }

  def entry(header: Component,
            body: Component,
            element: html.Element = dom.create[html.Div]("div")): AccordionEntry = {
    val e = new AccordionEntry(this, header, body, element)
    children += e
    e
  }
}