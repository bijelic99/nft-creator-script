trait Drawable {
    def imagePath: String
    def zIndex: Int
}

case class Attribute(
    imagePath: String,
    group: String,
    zIndex: Int
) extends Drawable

case class BaseEntity(
    imagePath: String,
    zIndex: Int
) extends Drawable

case class Entity(
    id: String,
    baseEntity: BaseEntity,
    imagePath: Option[String],
    attributes: Seq[Attribute]
)
