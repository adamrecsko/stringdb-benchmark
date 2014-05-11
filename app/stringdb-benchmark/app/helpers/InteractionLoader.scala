package helpers

import play.api.db.DB
import play.api.Play.current
import models.Interaction
import anorm._
import org.anormcypher._

/**
 * Created by adee on 2014.04.13..
 */
object InteractionLoader {
    implicit def string2Int(s: String): Int = augmentString(s).toInt
    def loadMysql(file:String) = {

      val lines = scala.io.Source.fromFile(file).getLines()

      DB.withConnection {
        implicit c =>
          SQL("delete from interactions").executeUpdate()
          lines.foreach(l => {
            val items = l.split(" ")
            SQL("INSERT INTO interactions values ({protein1},{protein2},{neighborhood},{fusion},{cooccurence},{coexpression},{experimental},{database},{textmining},{combined_score})")
              .on(
                'protein1 ->items(0),
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

  def loadNeo(file:  String) = {
    val lines = scala.io.Source.fromFile(file).getLines()
    println("Create Proteins")
    lines.foreach(l => {
      val items = l.split(" ")
      Cypher(
        """
          |CREATE (n:Interaction{ protein1: {p1}, protein2:{p2}, combined_score: {cs}  })
        """.stripMargin).on(
            "p1"->items(0),
            "p2"->items(1),
            "cs"->items(9)
        ).execute()

    })
    println("Create relations")
    Cypher(""" MATCH (a:Interaction), (b:Interaction) WHERE a.protein2=b.protein1 CREATE (a)-[:REL]->(b);
             | """.stripMargin).execute()

  }

}
