package LinkedDataVerse

import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js._
import org.scalajs.dom
import scala.util.Random
import scalatags.JsDom.all._
import dom.document

import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom.html

import LinkedDataVerse.world._

object App extends JSApp {

  lazy val el:HTMLElement = document.getElementById("board").asInstanceOf[HTMLElement]
  lazy val world = new MainScene(el, 640, 480)

  def main(): Unit = {
    appendP(document.body, "LinkedDataVerse")
    world.render()
  }

  def appendP(targetNode: dom.Node, text: String): Unit = {
    val parNode = document.createElement("p")
    val textNode = document.createTextNode(text)
    parNode.appendChild(textNode)
    targetNode.appendChild(parNode)
  }

  @JSExport
  def fetch(e: dom.Event): Unit = {

    val uri = document.querySelector("#uri").asInstanceOf[html.Input]

    val xhr = new dom.XMLHttpRequest()

    //xhr.open("GET", "http://www.w3.org/People/Berners-Lee/card#rdf", true)
    xhr.open("GET", uri.value, true)
    xhr.onload = (e: dom.Event) => {
      if (xhr.status == 200) {
        val res = JSON.parse(xhr.responseText)
        val lol = res.selectDynamic("http://dbpedia.org/ontology/Beverage")
        val label = lol
          .selectDynamic("http://www.w3.org/2000/01/rdf-schema#comment")
          .asInstanceOf[scalajs.js.Array[scalajs.js.Object]]
        world.addABox(JSON.stringify(label(0)))
        appendP(document.body, JSON.stringify(label(0)))
      }
    }
    xhr.send()

  }
}



