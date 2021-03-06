package it.callisto.scalacv

import javafx.beans.property.SimpleObjectProperty
import javafx.concurrent.Task
import javafx.geometry.Orientation
import javafx.event.Event
import javafx.event.EventHandler
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.control.Slider
import javafx.scene.layout.HBox
import javafx.scene.text.Font
import scala.concurrent._
import javafx.application.Platform
import javafx.util.Callback
import java.util.concurrent.Executor

object JfxExecutionContext {

  implicit val jfxExecutionContext: ExecutionContext = ExecutionContext.fromExecutor(new Executor {
    def execute(command: Runnable): Unit = Platform.runLater(command)
  })

}

trait JfxUtils {

  def mkCellFactoryCallback[T](listCellGenerator: ListView[T] => ListCell[T]) = new Callback[ListView[T], ListCell[T]]() {
    override def call(list: ListView[T]): ListCell[T] = listCellGenerator(list)
  }

  def mkEventHandler[E <: Event](f: E ⇒ Unit) = new EventHandler[E] {
    def handle(e: E) = f(e)
  }

  def mkTask[X](callFn: ⇒ X): Task[X] = new Task[X] {
    override def call(): X = callFn
  }

  def mkTop: HBox = {
    val hbox = new HBox()
    hbox.setStyle("-fx-padding: 15;" +
      "-fx-background-color: #333333," +
      "linear-gradient(#f3f3f3 0%, #ced3da 100%);" +
      "-fx-background-insets: 0, 0 0 1 0;")
    hbox
  }

  def mkSlider(min: Double, max: Double, initialValue: Double, orientation: Orientation, mtu: Int = 100): Slider = {
    require(min <= initialValue)
    require(initialValue <= max)
    val slider = new Slider()
    slider.setMin(min)
    slider.setMax(max)
    slider.setValue(initialValue)
    slider.setShowTickLabels(true)
    slider.setShowTickMarks(true)
    slider.setMajorTickUnit(mtu)
    slider.setMinorTickCount(mtu / 5)
    slider.setBlockIncrement(1)
    slider.setOrientation(orientation)
    slider
  }

  def sliderValue(): Label = {
    val l = new Label()
    l.fontProperty().setValue(Font.font("Verdana", 14))
    l.setMinWidth(75)
    l
  }

  def sliderText(text: String) = {
    val l = new Label()
    l.fontProperty().setValue(Font.font("Verdana", 14))
    l.textProperty.set(text)
    l
  }

}