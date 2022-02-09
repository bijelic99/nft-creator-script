import $file.Models
import $file.JsonImplicits
import $file.Configuration
import $file.Util
import $file.ImageProcessing
import $ivy.`com.typesafe.play::play-json:2.9.2`

import JsonImplicits._
import play.api.libs.json.Json
import java.io.FileInputStream
import Models.{BaseEntity, Attribute, Entity}
import Util._
import scala.util.Random
import java.util.UUID
import java.io.FileWriter
import java.nio.file.Path
import java.io.File

val baseEntity =
  Json.parse(new FileInputStream("./resources/base-entity.json")).as[BaseEntity]
val loadedAttributes = Json
  .parse(new FileInputStream("./resources/attributes.json"))
  .as[Seq[Attribute]]
  

if (Configuration.maxAttributes > loadedAttributes.map(_.group).distinct.size)
  throw new Exception("'maxAttributes' can't be larger than 'attributes.flatMap(_.map(_.group)).distinct.size'")

if (Configuration.minAttributes > Configuration.maxAttributes || Configuration.minAttributes < 0)
  throw new Exception("'minAttributes' can't be larger than 'maxAttributes', or less than 0")

val attributes = loadedAttributes.map(Some(_)) ++ Iterator.continually(None).take(Configuration.minAttributes).toSeq


val entities = CharthesianGenerator
 .recursiveCharthesian(attributes.map(Seq(_)), attributes, Configuration.maxAttributes)
 .filter(x => x.filter(_.nonEmpty).distinct.size == x.filter(_.nonEmpty).size)
 .filter{ combination =>
  combination.flatMap(_.toSeq).map(_.group).distinct.size == combination.flatMap(_.toSeq).size
}
.map(_.flatten)
.filter(_.size >= Configuration.minAttributes)
.map(combination => Entity(UUID.randomUUID().toString(), baseEntity, None, combination))
.map(ImageProcessing.drawEntity(_, "./output/images"))

val file = new File("./output/entities.json")
file.getParentFile().mkdirs()
file.createNewFile()
val writer = new FileWriter(file)

writer.write(Json.toJson(entities).toString())
 
writer.close()