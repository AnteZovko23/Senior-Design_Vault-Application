package com.example.webviewtest;

public class Link extends Volley
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    RequestQueue queue = Volley.newRequestQueue(this);
    String url = "http://localhost:8081/";

    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            new Response.Listener() {
                @Override
                public void onResponse(JSONObject response)
                {

                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error)
                {

                }
            });
    requestQueue.add(jsonObjectRequest);

}
}