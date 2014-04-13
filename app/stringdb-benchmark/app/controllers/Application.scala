package controllers

import play.api._
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.future
import scala.concurrent.blocking
import play.api.db.DB
import helpers.Measure
import play.api.Play.current
import anorm._
import play.api.libs.json.Json



object Application extends Controller {
  def index = Action {
    Ok(views.html.index("..."))
  }


  def getProteinCount = Action.async{
     future{
       DB.withConnection {
         implicit conn =>
           val row = SQL("SELECT count(protein1) as c FROM interactions").apply().head
           val count = row[Long]("c")
           val res = Json.obj(
              "value"->count
           )
           Ok(Json.stringify(res))
       }
     }


  }
  def benchMysql = Action.async {
         future{
          val time =  Measure.time({
             val start = System.currentTimeMillis;
             DB.withConnection {
               implicit conn =>
                   SQL("SELECT count(i2.protein2) FROM interactions AS i1  INNER JOIN interactions AS i2 ON i1.protein2 = i2.protein1").execute()
             }
           })
           val time2 =  Measure.time({
             val start = System.currentTimeMillis;
             DB.withConnection {
               implicit conn =>
                 SQL("SELECT count(i2.protein2) FROM interactions AS i1  INNER JOIN interactions AS i2 ON i1.protein2 = i2.protein1 order by i1.combined_score").execute()
             }
           })



          val res =  Json.arr(
              Json.obj(
                 "description"->"SELECT count(i2.protein2) FROM interactions AS i1  INNER JOIN interactions AS i2 ON i1.protein2 = i2.protein1",
                 "label" -> "Count all protein interactions",
                 "value" -> time
              ),
            Json.obj(
              "description"->"SELECT count(i2.protein2) FROM interactions AS i1  INNER JOIN interactions AS i2 ON i1.protein2 = i2.protein1 order by combined_score",
              "label" -> "order by combined_score",
              "value" -> time2
            )
            )
           Ok(Json.stringify(res))

         }


  }

}