package edu.self.web.publisher;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.FileUtils;

@Path("/")
public class ViewResolver {
    private String root;

    public static void main(String[] args) throws IOException {
        System.out.println(new ViewResolver().root);
    }

    public ViewResolver() throws IOException {
        File cur = new File(".");
        root = cur.getCanonicalPath() + "\\src\\main\\webapp";
    }

    @Context
    UriInfo uriInfo;
    @Context
    Request request;

    //it works
//    @GET
//    @Path("/rest/{path: .+}")
//    public Response redirectGet(@PathParam("path") String path) throws URISyntaxException {
//        return Response.seeOther(new URI(path)).build();
//    }

    //it does not word
//     @POST
//     @Path("/rest/{path: .+}")
//     public Response redirectPost(@PathParam("path") String path) throws URISyntaxException {
//         return Response.seeOther(new URI(path)).build();
//     }


    @GET
    @Path("/{file}")
    public Response getResource(@PathParam("file") String fileName) {
        try {
            File file = new File(root + "\\" + fileName);
            String content = FileUtils.readFileToString(file);
            content = content.replaceAll("<%.+?%>", ""); //delete directives
            content = content.replaceAll("rest/", ""); //quick fix for rest queries
            return Response.status(200).entity(content).header("Content-type", getContentType(fileName)).build();
        } catch (IOException e) {
            return Response.status(403).build();
        }
    }

    private String getContentType(String fileName) {
        if (fileName.matches(".*\\.(jsp|html)$")) {
            return "text/html; charset=utf-8";
        }
        if (fileName.matches(".*\\.(css)$")) {
            return "text/css";
        }
        if (fileName.matches(".*\\.(js)$")) {
            return "text/javascript";
        }
        return MediaType.TEXT_PLAIN;
    }

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getDefaultResource() {
        return getResource("index.jsp");
    }

    @GET
    @Path("/test")
    @Produces("text/plain")
    public String test() {
        String output = "Resource Test";
        return output;
    }
}
