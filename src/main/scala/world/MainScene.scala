package LinkedDataVerse.world

import scala.scalajs.js
import org.denigma.threejs._
import org.denigma.threejs.extensions.Container3D
import org.denigma.threejs.extensions.controls.CameraControls
import LinkedDataVerse.controls.NavControls

import org.scalajs.dom.raw.HTMLElement
import scala.util.Random

import org.scalajs.dom.html
import org.scalajs.dom
import dom.document

class MainScene(
  val container:HTMLElement,
  var width:Double,
  var height:Double) extends Container3D {

  override def distance = 15
  override val controls = new NavControls(camera, this.container)

  private def randPos() = new Vector3(
    Random.nextInt(10) - 5,
    Random.nextInt(10) - 5,
    -5 - Random.nextInt(10))

  def addABox(text: String) {
    val testText = TextPlane(text)
    testText.position.copy(randPos())
    scene.add(testText)
  }

  WorldHelper.addLights(scene);

  val boxGeom = new BoxGeometry(1, 1, 1)

  val plainMaterial = new MeshLambertMaterial(js.Dynamic.literal(
    color = new Color().setHex(0xffffff)
  ).asInstanceOf[MeshLambertMaterialParameters])

  val meshes:Seq[Mesh] = Range(0, 10).map(i => {

    val mesh = new Mesh(boxGeom, plainMaterial)
    mesh.position.copy(randPos())
    scene.add(mesh)
    mesh

  })

  // Derp, scala.js is forcing me to used LineDashed...
  // https://github.com/antonkulaga/scala-js-facades/issues/2
  val lineMaterial = new LineDashedMaterial(js.Dynamic.literal(
    color = new Color().setHex(0x0088ff)
  ).asInstanceOf[LineDashedMaterialParameters]);

  val lineGeo = new Geometry();
  meshes.foldLeft (meshes(0)) { (ac, el) =>
    lineGeo.vertices.push(el.position.clone());

    el
  }

  scene.add(new Line(lineGeo, lineMaterial));

  val img = ImgUrMesh("dAvWkN8.jpg")
  img.position.set(2, 2, -5)
  scene.add(img)

  val projector = new Projector()
  val raycaster = new Raycaster()

  def findIntersections(x:Double, y:Double) = {

    val vector = new Vector3(x, y, 1)
    projector.unprojectVector(vector, camera)

    raycaster
      .set(camera.position, vector.sub(camera.position).normalize())

    raycaster
      .intersectObjects(scene.children)
      .sortWith((a, b) => a.point.distanceTo(vector) < b.point.distanceTo(vector))
      .toList
  }

  def onCursorMove(clientX:Double, clientY:Double, width:Double, height:Double) = {

    val mouseX = ( clientX / width) * 2 - 1
    val mouseY = - ( clientY / height ) * 2 + 1

    val intersections = findIntersections(mouseX, mouseY)
    val underMouse = intersections.groupBy(_.`object`).toMap

    underMouse

  }

  override def onEnterFrame() {

    super.onEnterFrame()

    camera.position.z += (Math.sin(java.lang.System.currentTimeMillis() / 800.0) * 0.01)

    meshes(1).rotation.y += 0.01;
    meshes(2).rotation.y += 0.015;

    val hits = onCursorMove(
      controls.lastMousePos._1,
      controls.lastMousePos._2,
      width,
      height);

    if (!hits.isEmpty) {
      hits.head._1.position.z -= 0.05;
    }


    // return 0.5 * ( 1 - Math.cos( Math.PI * k ) );
    ///val hmm = js.Dynamic.literal.applyDynamic("TWEEN.Easing.Sinusoidal.InOut") _

  }

  def onResize () = {
    width = dom.window.innerWidth
    camera.aspect = dom.window.innerWidth / height
    camera.updateProjectionMatrix()
    renderer.setSize(dom.window.innerWidth, height);
    width
  }
  dom.window.addEventListener("resize", (e:dom.Event) => onResize(), false)
  onResize()

}
