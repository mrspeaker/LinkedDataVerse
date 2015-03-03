package RDFExplorer.scenes;

import scala.scalajs.js
import org.denigma.threejs._
import org.denigma.threejs.extensions.Container3D

import org.scalajs.dom.raw.HTMLElement
import scala.util.Random

import org.scalajs.dom.html
import org.scalajs.dom
import dom.document

class ThreeScene(val container:HTMLElement, val width:Double, val height:Double) extends Container3D {

  override def distance = 15

  def TextPlane (text: String): Mesh = {

    val canvas = document.createElement("canvas").asInstanceOf[html.Canvas]
    val ctx = canvas.getContext("2d")
    ctx.width = 256
    ctx.height = 80
    ctx.textAlign = "center";
    ctx.font = "22pt Helvetica"
    ctx.fillStyle = "#000000"
    ctx.fillText(text, 256/2, 30)

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

  // Some lights
  val dirLight1 = new DirectionalLight(0xffffff, 0.9)
  dirLight1.position.set(1, 1, 1.5).normalize()
  scene.add(dirLight1)

  val dirLight2 = new DirectionalLight(0x9999ff, 0.5)
  dirLight2.position.set(-1, -1, -1).normalize()
  scene.add(dirLight2)

  val ambLight = new AmbientLight(0x434343)
  scene.add(ambLight);

  scene.fog = new Fog(0xffffff, 10, 25);

  val boxGeom = new BoxGeometry(1, 1, 1)

  def materialParams(col: Int) = js.Dynamic.literal(
    color = new Color().setHex(col)//,
    //wireframe = true
  ).asInstanceOf[MeshLambertMaterialParameters]

  val plainMaterial = new MeshLambertMaterial(materialParams(0xffffff))

  val space = 1.5
  val meshes:Seq[Mesh] = Range(0, 25).map(i => {

    val mesh = new Mesh(boxGeom, plainMaterial)
    mesh.position.copy(new Vector3(
      Random.nextInt(10) - 5,
      Random.nextInt(10) - 5,
      -5 - Random.nextInt(10)))
    scene.add(mesh)
    mesh

  })

  // Derp, scala.js is forcing me to used LineDashed... LineBasic is complaining?
  val lineMaterial = new LineDashedMaterial(js.Dynamic.literal(
    color = new Color().setHex(0x0088ff)
  ).asInstanceOf[LineDashedMaterialParameters]);

  val lineGeo = new Geometry();
  meshes.foldLeft (meshes(0)) { (ac, el) =>
    // TODO: make right angles to dest.
    lineGeo.vertices.push(el.position.clone());

    el
  }

  val line = new Line(lineGeo, lineMaterial);
  scene.add(line);

  Range(0, 5).map(i => {

    val testText = TextPlane("Test text " + i)
    testText.position.copy(new Vector3(
      Random.nextInt(10) - 5,
      Random.nextInt(10) - 5,
      -5 - Random.nextInt(10)))
    scene.add(testText)

  })

  //camera.position.set(10, 15, 10);

  override def onEnterFrame() {

    super.onEnterFrame()

    camera.position.z = 10 + (Math.sin(java.lang.System.currentTimeMillis() / 800.0) * 2)

    meshes(1).rotation.y += 0.01;
    meshes(2).rotation.y += 0.015;

  }

}
