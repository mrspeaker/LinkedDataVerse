package LinkedDataVerse.world

import scala.scalajs.js
import org.denigma.threejs._

import org.scalajs.dom.html
import org.scalajs.dom
import dom.document

object TextPlane {

  def apply (text: String): Mesh = {

    val canvas = document.createElement("canvas").asInstanceOf[html.Canvas]
    val ctx = canvas.getContext("2d")
    ctx.width = 256
    ctx.height = 128
    ctx.textAlign = "center";
    ctx.font = "22pt Helvetica"
    ctx.fillStyle = "#000000"
    ctx.fillText(text, 256 / 2, 30)

    val texture = new Texture(canvas);
    texture.needsUpdate = true;

    val canMaterial = new MeshBasicMaterial(js.Dynamic.literal(
      map = texture,
      transparent = true
    ).asInstanceOf[MeshBasicMaterialParameters]);

    val canGeometry = new PlaneGeometry(canvas.width, canvas.height, 1, 1);
    val planeMesh = new Mesh(canGeometry, canMaterial);
    planeMesh.scale.set(0.01, 0.01, 0.01);

    planeMesh

  }

}