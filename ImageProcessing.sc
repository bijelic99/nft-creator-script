import $ivy.`com.sksamuel.scrimage:scrimage-core:4.0.26`
import $ivy.`com.sksamuel.scrimage::scrimage-scala:4.0.26`
import $file.Models

import Models.Entity
import com.sksamuel.scrimage.ImmutableImage
import com.sksamuel.scrimage.nio.ImageWriter
import com.sksamuel.scrimage.nio.PngWriter
import java.nio.file.Path

def drawEntity(entity: Entity, folder: String): Entity = {
    val entityImage = ImmutableImage.loader().fromFile(entity.baseEntity.imagePath).copy()
    entity.attributes.foreach{ attribute =>
        val attributeImage = ImmutableImage.loader.fromFile(attribute.imagePath)
        entityImage.overlay(attributeImage, 0, 0)    
    }
    val savePath = entityImage.output(PngWriter.NoCompression, Path.of(folder, s"${entity.id}.png"))
    entity.copy(imagePath = Some(savePath.toString()))
}