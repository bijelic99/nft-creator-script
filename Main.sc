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
import org.apache.commons.io.FileUtils

FileUtils.deleteDirectory(new File("./output"))

val baseEntity =
  Json.parse(new FileInputStream("./resources/base-entity.json")).as[BaseEntity]
val loadedAttributes = Json
  .parse(new FileInputStream("./resources/attributes.json"))
  .as[Seq[Attribute]]
  

if (Configuration.combinations > loadedAttributes.map(_.group).distinct.size)
  throw new Exception("'combinations' can't be larger than 'attributes.flatMap(_.map(_.group)).distinct.size'")


val entities = 
loadedAttributes
.combinations(Configuration.combinations)
.filter(combinations => combinations.distinctBy(_.group).size == Configuration.combinations)
.map(combination => Entity(UUID.randomUUID().toString(), baseEntity, None, combination))
.map(ImageProcessing.drawEntity(_, "./output/images"))
.toSeq

val file = new File("./output/entities.json")
file.getParentFile().mkdirs()
file.createNewFile()
val writer = new FileWriter(file)

writer.write(Json.prettyPrint(Json.toJson(entities)))
 
writer.close()