import React from "react"
import {Link} from "react-router-dom";
import '../styles/navbar.css'
import {navBarResources} from "../routing/config"

export const Navbar = () => {

  return (
      <nav className={"navbar"}>
        {navBarResources.map(
            route =>
                <Link key={`${route.type} route`} className={"links"} to={`/${route.type}`}>
                  {route.name}
                </Link>
        )}
      </nav>
  )
}