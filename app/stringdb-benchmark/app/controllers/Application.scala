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
import org.anormcypher._
import play.api.libs.json.{JsValue, JsArray, Json}
import models.{Benchmarks, Interaction}
import scala.util.Random
import scala.collection.immutable.HashSet


object Application extends Controller {
  def index = Action {
    Ok(views.html.index("..."))
  }


  def getProteinCount = Action.async{
     future{
       DB.withConnection {
         implicit conn =>
           val row = SQL("SELECT count(*) as c FROM interactions").apply().head
           val count = row[Long]("c")
           val res = Json.obj(
              "value"->count
           )
           Ok(Json.stringify(res))
       }
     }
  }


  def getGraph(protein:String, limit:Int) = Action.async{
       future{
         DB.withConnection {
           implicit conn =>
             val list = SQL("SELECT i1.protein1, i1.protein2, i1.combined_score FROM interactions AS i1  INNER JOIN interactions AS i2 ON i1.protein2 = i2.protein1  WHERE i1.protein1 = {p1} order by i1.combined_score desc limit {limit}").on(
               'p1 -> protein,
               'limit -> limit
             )().map {
                row =>  new  Interaction(row[String]("protein1"),row[String]("protein2"),0,0,0,0,0,0,0,row[Int]("combined_score"))
             }
           val rnd = new Random
           val  _nodes = (for ( inter <- list) yield Json.obj(
             "id" -> inter.protein2,
             "label" -> inter.protein2,
             "size" -> 3,
             "x"->rnd.nextInt(10),
             "y"->rnd.nextInt(10)
           ))
           val source = Json.obj(
             "id" -> protein,
             "label" -> protein,
             "size" -> 5,
             "x"->0,
             "y"->0
           )
           val nodes =  Json.toJson( source::_nodes.toList );
           val edges =  Json.toJson(  for ( inter <- list) yield Json.obj(
               "id" -> (inter.protein2 + inter.protein2),
               "source" -> inter.protein1,
               "target" -> inter.protein2
             ) )
            val res = Json.obj("nodes"->nodes,"edges"->edges)
            Ok(res).as("application/json")

         }
       }
  }



  def benchMysql = Action.async {
         future{
           Ok(Json.stringify(Benchmarks.getAll))
         }
  }


  def runBench(name:String) = Action.async {
        future{
           Ok(Benchmarks.runBench(name)).as("application/json")
        }

  }


  def benchNeo = Action.async{

      future{
        val req = Cypher("""
           MATCH  (n:Interaction)-[r]-(m:Interaction) RETURN n.protein1 as nprotein, TYPE(r) as rtype, m.protein1 as mprotein, ID(n) as nid, ID(m) as mid, ID(r)
        as rid limit 2000 """)


        val stream = req()
        val l = stream.collect( {
          case CypherRow(nprotein:String,rtype:String,mprotein:String,nid:BigDecimal,mid:BigDecimal,rid:BigDecimal) =>{
            val protein1 =  Json.obj(
                "id"   ->  mid,
                "name" ->  nprotein,
                "labels" -> Json.arr("Protein")
            )
            val protein2 = Json.obj(
              "id"   ->  mid,
              "name" ->  mprotein,
              "labels" -> Json.arr("Protein")
            )
            List(protein1,protein2)

          }
        }).flatMap( x=> List(x(0),x(1)) ).toSet.toList
        val rel = stream.collect( {
          case CypherRow(nprotein:String,rtype:String,mprotein:String,nid:BigDecimal,mid:BigDecimal,rid:BigDecimal) =>{
            val start = l.indexWhere(p => {
              (p\"id" ).as[BigDecimal].equals( nid )
            })
            val end = l.indexWhere(p => {
              (p\"id" ).as[BigDecimal].equals( mid )
            })
            val relation = Json.obj(
              "type" ->rtype,
              "start" -> start,
              "end" -> end,
              "source"->start,
              "target"->end,
              "id"->rid
            )
             relation
          }
        }).toList








        val res = Json.obj(
           "nodes" ->Json.toJson( l  ),
           "links" ->Json.toJson ( rel)
        )
        Ok(res).as("JSON")



      }


  }


}










































