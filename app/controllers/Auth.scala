package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import views._
import models._

object Auth extends Controller {

  val loginForm = Form(
    tuple(
        "email"    -> nonEmptyText,
        "password" -> nonEmptyText
    ) verifying ("Invalid email or password", result => result match {
      case (email, password) => User.authenticate( email, password ).isDefined
    })
  )

  /* create new user */
  val createForm = Form (
    tuple(
        "email" -> nonEmptyText,
        "name"  -> nonEmptyText,
        "passw1"-> nonEmptyText,
        "passw2"-> nonEmptyText
    ) verifying ("Email already exists", result => result match {
        case(email,_,_,_) => ! User.findByEmail( email ).isDefined
    })
      verifying("Name already exists", result => result match {
        case(_,name,_,_) => ! User.findByName( name ).isDefined
      })
  )

  /*
   * serves the login page
   */
  def createUser = Action { implicit request =>
    Ok(html.createUser(createForm)) 
  }
  
  /*
   * verifies that the user is not already taken. 
   * If so, logs him in.
   */
  def newUser = Action { implicit request =>
    createForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(html.createUser(formWithErrors))
      },

      user => {
          User.create(User(user._1, user._2, user._3))
          Redirect(routes.Users.index(user._2)).withSession("email" -> user._1)
      }
    )
  }

  /* serves the login form */ 
  def login = Action { implicit request =>
    Ok(html.login(loginForm))
  }
  
  /*
   * Authenticates the user after submiting email and password
   */
  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.login(formWithErrors)),
      user => User.findByEmail( user._1 ).map { usr =>
        Redirect(routes.Users.index( usr.name )).withSession("email" -> user._1)
      }.getOrElse(Forbidden)
    )
  }

  /* 
   * Logs the user out
   */
  def logout = Action {
    Redirect(routes.Auth.login).withNewSession.flashing(
      "success" -> "You are now logged out."
    )
  }
}
