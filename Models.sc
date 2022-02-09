case class Attribute(
    imagePath: String,
    group: String
)

case class BaseEntity(
    imagePath: String
)

case class Entity(
    id: String,
    baseEntity: BaseEntity,
    imagePath: Option[String],
    attributes: Seq[Attribute]
)
