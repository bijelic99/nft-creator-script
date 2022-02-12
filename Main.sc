import $file.Models
import $file.JsonImplicits
import $file.Configuration
import $file.ImageProcessing
import $ivy.`com.typesafe.play::play-json:2.9.2`
import $ivy.`com.google.guava:guava:31.0.1-jre`

import JsonImplicits._
import play.api.libs.json.Json
import java.io.FileInputStream
import Models.{BaseEntity, Attribute, Entity}
import scala.util.Random
import java.util.UUID
import java.io.FileWriter
import java.nio.file.Path
import java.io.File
import org.apache.commons.io.FileUtils
import com.google.common.collect.Sets
import scala.jdk.CollectionConverters._
import java.{util => ju}

FileUtils.deleteDirectory(new File("./output"))

val baseEntity =
  Json.parse(new FileInputStream("./resources/base-entity.json")).as[BaseEntity]
val loadedAttributes = Json
  .parse(new FileInputStream("./resources/attributes.json"))
  .as[Seq[Attribute]]

if (Configuration.combinations > loadedAttributes.map(_.group).distinct.size)
  throw new Exception(
    "'combinations' can't be larger than 'attributes.flatMap(_.map(_.group)).distinct.size'"
  )

val combinations = loadedAttributes.groupBy(_.group).size

val entities =
  Sets
    .cartesianProduct[Attribute](
      ju.stream.StreamSupport
        .stream(
          loadedAttributes
            .groupBy(_.group)
            .map { case key -> value =>
              key -> new ju.HashSet[Attribute](value.asJava)
            }
            .values
            .asJava
            .spliterator(),
          false
        )
        .collect(ju.stream.Collectors.toList())
    )
    .asScala
    .toSeq
    .map(_.asScala.toSeq)
    .map(combination =>
      Entity(UUID.randomUUID().toString(), baseEntity, None, combination)
    )
    .map(ImageProcessing.drawEntity(_, "./output/images"))
    .toSeq

val file = new File("./output/entities.json")
file.getParentFile().mkdirs()
file.createNewFile()
val writer = new FileWriter(file)

writer.write(Json.prettyPrint(Json.toJson(entities)))

writer.close()
