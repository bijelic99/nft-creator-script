import $ivy.`com.sksamuel.scrimage:scrimage-core:4.0.26`
import $ivy.`com.sksamuel.scrimage::scrimage-scala:4.0.26`
import $file.Models

import Models.Entity
import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.nio.ImageWriter
import com.sksamuel.scrimage.nio.PngWriter
import java.nio.file.Path
import java.io.File

import scala.util.chaining._

def drawEntity(entity: Entity, folder: String): Entity = {
  val baseImageCopy =
    ImmutableImage.loader().fromFile(entity.baseEntity.imagePath).copy()

  val entityImage = entity.attributes.foldLeft(baseImageCopy) {
    (image, attribute) =>
      val attributeImage = ImmutableImage.loader.fromFile(attribute.imagePath)
      image.overlay(attributeImage, 0, 0)
  }
  val file = new File(Path.of(folder, s"${entity.id}.png").toString())
  file.getParentFile().mkdirs()
  file.createNewFile()
  val savePath = entityImage.output(PngWriter.NoCompression, file)
  entity.copy(imagePath = Some(savePath.toString()))
}
