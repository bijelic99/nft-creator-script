case class Attribute(
                      imagePath: String,
                      group: String
                    )

case class BaseEntity(
  imagePath: String
)

case class Entity(
                   checksum: String,
                   imagePath: String,
                   attributes: Seq[Attribute]
                 )