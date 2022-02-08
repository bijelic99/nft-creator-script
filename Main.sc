import $file.Models
import $file.JsonImplicits
import $file.Configuration
import $file.Util
import $ivy.`com.typesafe.play::play-json:2.9.2`

import JsonImplicits._
import play.api.libs.json.Json
import java.io.FileInputStream
import Models.{BaseEntity, Attribute, Entity}
import Util.ChecksumGenerator
import scala.util.Random

val baseEntity =
  Json.parse(new FileInputStream("./resources/base-entity.json")).as[BaseEntity]
val attributes = Json
  .parse(new FileInputStream("./resources/attributes.json"))
  .as[Seq[Attribute]]

if (attributes.isEmpty)
  throw new Exception("At least one attribute is required")

if (Configuration.maxAttributes > attributes.map(_.group).distinct.size)
  throw new Exception("'maxAttributes' can't be larger than 'attributes.map(_.group).distinct.size'")

if (Configuration.minAttributes > attributes.map(_.group).distinct.size)
  throw new Exception("'minAttributes' can't be larger than 'attributes.map(_.group).distinct.size'")

if (Configuration.minAttributes > Configuration.maxAttributes)
  throw new Exception("'minAttributes' can't be larger than 'maxAttributes'")

val ees = Iterator
  .iterate(Seq.empty[Entity]) { entities =>
    val numberOfAttributes =
      Random.between(Configuration.minAttributes, Configuration.maxAttributes)
    val entityAttributes = Iterator
      .iterate(Seq.empty[Attribute]) { attSeq =>
        val attribute = attributes(Random.between(0, attributes.size - 1))
        attSeq
          .find(_.group.equals(attribute.group))
          .fold(attSeq.+:(attribute))(_ => attSeq)
      }
      .find(_.size == numberOfAttributes)
      .toSeq
      .flatten
      .toSeq
    val entity = Entity(
      checksum = ChecksumGenerator.generateChecksum(
        entityAttributes.map(_.imagePath).+:(baseEntity.imagePath): _*
      ),
      imagePath = baseEntity.imagePath,
      attributes = entityAttributes
    )
    entities
      .find(_.checksum.equals(entity.checksum))
      .fold(entities.+:(entity))(_ => entities)
  }
  .find(_.size == Configuration.numberOfEntities)
  .toSeq
  .flatten
  .toSeq

println(ees)
