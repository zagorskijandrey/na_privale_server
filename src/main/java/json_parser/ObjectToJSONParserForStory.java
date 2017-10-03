package json_parser;

import service.get.GetStoryData;
import model.Story;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * Created by AZagorskyi on 14.04.2017.
 */
public class ObjectToJSONParserForStory {

    @SuppressWarnings("unchecked")
    public JSONObject getJSONStory(String sqlQuery, String story_id){
        JSONObject jsonObject = new JSONObject();
        GetStoryData storyData = new GetStoryData(sqlQuery, story_id);
        Story story = storyData.getStory();
        jsonObject.put("id", story.getId());
        jsonObject.put("name", story.getName());
        jsonObject.put("text", story.getText());
        jsonObject.put("author", story.getAuthor());
        return jsonObject;
    }

    @SuppressWarnings("unchecked")
    public JSONObject getJSONObjectStories(String sqlQuery, int start,int total){
        GetStoryData storyData = new GetStoryData(sqlQuery);
        ArrayList<Story> storiesList = storyData.getStoriesList(start, total);
        JSONArray jsonArray = new JSONArray();
        for (Story story: storiesList){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", story.getId());
            jsonObject.put("name", story.getName());
            jsonObject.put("text", getSubstringText(story));
            jsonObject.put("author", story.getAuthor());
            jsonArray.add(jsonObject);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("stories_list", jsonArray);
        jsonObject.put("count_stories", storyData.getCountStories());
        return jsonObject;
    }

    private String getSubstringText(Story story){
        String text = story.getText();
        if (text.length() > 500){
            text = text.substring(0, 500).concat("...");
        }
        return text;
    }
}
