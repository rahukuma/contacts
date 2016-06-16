package com.rahulk.mockserver.contacts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rahulk.mockserver.contacts.message.ContactDTO;
import com.rahulk.mockserver.contacts.message.Error.ErrorCode;
import com.rahulk.mockserver.contacts.message.Error.ErrorMessage;
import com.rahulk.utils.ErrorUtils;
import com.rahulk.utils.FileUtils;
import com.rahulk.utils.StringUtils;

import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Rahul Kumar on 13-05-16.
 */

@Path("/")
public class ContactRestApi {

    /**
     * @param contacts Having json with email mandatory.
     *                 Also email supplied is considered case sensitive
     * @return
     */
    @POST
    @Path("contacts")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postContacts(ContactDTO contacts) {
        // If email is not supplied the throw 400 bas request
        if (contacts.getEmail() == null) {
            String missingResponse = ErrorUtils.get(ErrorCode.EMAIL_MISSING, ErrorMessage.EMAIL_MISSING);
            return Response.status(Response.Status.BAD_REQUEST).entity(missingResponse).build();
        }
        // Generating ID which will be used for DB in real case.
        String id = StringUtils.getRandom();
        // Storing contact json in a file with name having name and email id
        // Using name and email so that searching by either param could be easy
        String fileName = contacts.getName() + "___" + contacts.getEmail() + ".txt";
        contacts.setId(id);

        // Setting null to optional and unsupplied param so using serializeNulls in Gson
        Gson gson = new GsonBuilder().serializeNulls().create();
        String responseMessage = gson.toJson(contacts);

        // Storing json response in a file on disk
        FileUtils fileUtils = new FileUtils();
        // Checking if user with the given email exists.If exists return error otherwise save the response
        if (fileUtils.exists(contacts.getEmail())) {
            String conflictResponse = ErrorUtils.get(ErrorCode.EMAIL_EXISTING,
                    String.format(ErrorMessage.EMAIL_EXISTING, contacts.getEmail()));
            return Response.status(Response.Status.CONFLICT).entity(conflictResponse).build();
        }
        fileUtils.save(responseMessage, FileUtils.PARENT_DATA_DIR, fileName);
        return Response.status(Response.Status.OK).entity(responseMessage).build();
    }

    @GET
    @Path("contacts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStatus(@QueryParam("name") String name,
                              @QueryParam("email") String email) {
        List<ContactDTO> contactDTOList = new ArrayList<>();
        Gson gson = new GsonBuilder().serializeNulls().create();
        // In case neither is given, error
        if (name == null && email == null) {
            String missingResponse = ErrorUtils.get(ErrorCode.NAME_EMAIL_EXISTING, ErrorMessage.NAME_EMAIL_EXISTING);
            return Response.status(Response.Status.BAD_REQUEST).entity(missingResponse).build();
        } else {
            FileUtils fileUtils = new FileUtils();
            String criteria;
            if (email != null) {
                criteria = email;
            } else {
                criteria = name;
            }
            // Search length should be less than 3
            if (criteria.length() < 3) {
                String errorResponse = ErrorUtils.get(ErrorCode.SEARCH_CRITERIA_LENGTH_INSUFFICIENT, ErrorMessage.SEARCH_CRITERIA_LENGTH_INSUFFICIENT);
                return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
            }
            if (fileUtils.exists(criteria)) {
                File[] files = fileUtils.getFiles(criteria);
                for (File file : files) {
                    System.out.println("FOUND FILE WITH NAME : " + file.getName() + " N PATH " + file.getAbsolutePath());
                    String response = fileUtils.read(file.toString());
                    contactDTOList.add(gson.fromJson(response, ContactDTO.class));
                }
            }
        }
        String finalResponse = gson.toJson(contactDTOList);
        return Response.status(Response.Status.OK).entity(finalResponse).build();
    }

    /**
     * If email or/and name is changed then stored file name is also changed since old data is not kept
     *
     * @param id
     * @param contacts
     * @return
     */
    @PUT
    @Path("contacts/email/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateContacts(@PathParam("id") String id,
                                   ContactDTO contacts) {
        if (id == null) {
            String missingResponse = ErrorUtils.get(ErrorCode.EMAIL_MISSING, ErrorMessage.EMAIL_MISSING);
            return Response.status(Response.Status.NOT_FOUND).entity(missingResponse).build();
        }
        FileUtils fileUtils = new FileUtils();
        String response = "";
        String newFileName;
        String oldFileName;
        if (fileUtils.exists(id)) {
            File[] files = fileUtils.getFiles(id);
            for (File file : files) {
                ContactDTO existingContacts = new GsonBuilder().serializeNulls().create().
                        fromJson(fileUtils.read(file.toString()), ContactDTO.class);
                oldFileName = existingContacts.getName() + "___" + existingContacts.getEmail() + ".txt";
                if (!existingContacts.getEmail().equals(contacts.getEmail())) {
                    if (!existingContacts.getName().equals(contacts.getName())) {
                        newFileName = contacts.getName() + "___" + contacts.getEmail() + ".txt";
                    } else {
                        newFileName = existingContacts.getName() + "___" + contacts.getEmail() + ".txt";
                    }
                } else {
                    if (!existingContacts.getName().equals(contacts.getName())) {
                        newFileName = contacts.getName() + "___" + existingContacts.getEmail() + ".txt";
                    } else {
                        newFileName = oldFileName;
                    }
                }
                response = new GsonBuilder().serializeNulls().create().toJson(contacts);
                fileUtils.save(response, FileUtils.PARENT_DATA_DIR, newFileName);
                if (!newFileName.equals(oldFileName)) {
                    fileUtils.delete(oldFileName);
                }
            }
        }
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @DELETE
    @Path("contacts/email/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteContacts(@PathParam("id") String id) {
        if (id == null) {
            String missingResponse = ErrorUtils.get(ErrorCode.EMAIL_MISSING, ErrorMessage.EMAIL_MISSING);
            return Response.status(Response.Status.NOT_FOUND).entity(missingResponse).build();
        }
        FileUtils fileUtils = new FileUtils();
        String response = null;
        if (fileUtils.exists(id)) {
            File[] files = fileUtils.getFiles(id);
            for (File file : files) {
                response = fileUtils.read(file.toString());
                file.delete();
            }
        } else {
            String missingResponse = ErrorUtils.get(ErrorCode.EMAIL_NOT_EXISTING, ErrorMessage.EMAIL_NOT_EXISTING);
            return Response.status(Response.Status.BAD_REQUEST).entity(missingResponse).build();
        }
        return Response.status(Response.Status.OK).entity(response).build();
    }

}


