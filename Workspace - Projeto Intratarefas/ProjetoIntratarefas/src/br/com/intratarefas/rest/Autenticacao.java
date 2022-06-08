/*package br.com.intratarefas.rest;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.sun.deploy.net.HttpRequest;

@Path("auth/")
public class Autenticacao extends UtilRest {

	@GET
	@Path("isActive/")
	public Response isActive(@Context HttpServletRequest req, HttpServletResponse res)
	{
		
		Object obj = req.getSession().getAttribute("login");
		
		return buildResponse(obj != null);
	}
}*/
