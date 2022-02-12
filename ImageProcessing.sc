import $ivy.`com.sksamuel.scrimage:scrimage-core:4.0.26`
import $ivy.`com.sksamuel.scrimage::scrimage-scala:4.0.26`
import $file.Models
import $file.Configuration

import Models.Entity
import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.nio.ImageWriter
import com.sksamuel.scrimage.nio.PngWriter
import java.nio.file.Path
import java.io.File

import scala.util.chaining._
import java.awt.Color
import com.sksamuel.scrimage.color.Colors

def drawEntity(entity: Entity, folder: String): Entity = {
  val layers: Seq[Models.Drawable] = entity.attributes :+ entity.baseEntity

  val outputImage =
    layers
      .sortBy(_.zIndex)(Ordering.Int)
      .zipWithIndex
      .map{
        case (drawable, index) if index == 0 =>
          ImmutableImage.loader.fromFile(drawable.imagePath).copy()
        case (drawable, _) =>
          ImmutableImage.loader.fromFile(drawable.imagePath)
      }
      .reduce((image1, image2) => image1.overlay(image2, 0, 0))

  val file = new File(Path.of(folder, s"${entity.id}.png").toString())
  file.getParentFile().mkdirs()
  file.createNewFile()

  val savePath = outputImage.output(PngWriter.NoCompression, file)
  entity.copy(imagePath = Some(savePath.toString()))
}
