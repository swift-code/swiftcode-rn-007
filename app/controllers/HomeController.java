package controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.ConnectionRequest;
import models.Profile;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.stream.Collectors;

/**
 * Created by lubuntu on 10/23/16.
 */
public class HomeController extends Controller {
    @Inject
    ObjectMapper objectMapper;
    public Result getprofile(Long Id){
        User user = User.find.byId(Id);
        Profile profile = user.profile.find.byId(Id);
        ObjectNode data = objectMapper.createObjectNode();
        data.put("firstname",profile.firstname);
        data.put("lastname",profile.lasttname);
        data.put("id",user.id);
        data.put("email",user.email);

        data.set("connections", objectMapper.valueToTree(user.connections.stream().map(connection->{
            ObjectNode connectionJson = objectMapper.createObjectNode();
            User connectionUser = User.find.byId(connection.id);
            Profile connectionProfile = Profile.find.byId(connection.profile.id);

            connectionJson.put("firstname", connectionProfile.firstname);
            connectionJson.put("lastname", connectionProfile.lasttname);
            connectionJson.put("id", connectionUser.id);
            connectionJson.put("email",connectionUser.email);
            connectionJson.put("company",connectionProfile.company);
            return (connectionJson);
        }).collect(Collectors.toList())));

        data.set("connectionRequests", objectMapper.valueToTree(user.ConnectionRequestReceived.stream().filter(x->x.status.equals(ConnectionRequest.Status.WAITING)).map(connectionRequest->{
            ObjectNode connectionRequestJson = objectMapper.createObjectNode();
            Profile connectionRequestProfile = Profile.find.byId(connectionRequest.sender.profile.id);
            connectionRequestJson.put("firstname", connectionRequestProfile.firstname);
            connectionRequestJson.put("id", connectionRequest.id);
            return (connectionRequestJson);
        }).collect(Collectors.toList())));
        User.find.all().stream()
                .filter(x->!user.equals(x))
                .filter(x->!user.connections.contains(x))
                .filter(x->!user.ConnectionRequestReceived.stream().map(y->y.sender).collect(Collectors.toList()).contains(x))
                .filter(x->!user.ConnectionRequestSent.stream().map(y->y.receiver).collect(Collectors.toList()).contains(x));
        return ok(data);
    }


}
