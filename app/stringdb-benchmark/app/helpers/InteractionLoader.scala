package helpers

import play.api.db.DB
import play.api.Play.current
import models.Interaction
import anorm._
/**
 * Created by adee on 2014.04.13..
 */
object InteractionLoader {
    implicit def string2Int(s: String): Int = augmentString(s).toInt
    def load(file:String) = {

      val lines = scala.io.Source.fromFile(file).getLines()

      DB.withConnection {
        implicit c =>
          SQL("delete from interactions").executeUpdate()
          lines.foreach(l => {
            val items = l.split(" ")
            val interaction = new Interaction(
              items(0),
              items(1),
              items(2),
              items(3),
              items(4),
              items(5),
              items(6),
              items(7),
              items(8),
              items(9)
            )


          SQL("INSERT INTO interactions values ({protein1},{protein2},{neighborhood},{fusion},{cooccurence},{coexpression},{experimental},{database},{textmining},{combined_score})")
            .on(
              'protein1 -> items(0),
              'protein2 -> items(1),
              'neighborhood -> items(2),
              'fusion -> items(3),
              'cooccurence ->items(4),
              'coexpression -> items(5),
              'experimental -> items(6),
              'database -> items(7),
              'textmining -> items(8),
              'combined_score -> items(9)

            ).executeInsert()


          }
          )

      }
  }

}
