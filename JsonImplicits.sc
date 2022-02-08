import play.api.libs.json.Format
import play.api.libs.json.OFormat
import $file.Models
import $ivy.`com.typesafe.play::play-json:2.9.2`
import play.api.libs.json.Json

import Models._

implicit val attributeFormat: Format[Attribute] = Json.format[Attribute]
implicit val baseEntityFormat: Format[BaseEntity] = Json.format[BaseEntity]
implicit val entityFormat: Format[Entity] = Json.format[Entity]