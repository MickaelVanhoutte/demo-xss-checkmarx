# Sanitizer validator   

Demo project made for Checkmarx
The point is to demonstrate that the incoming data is escaped for XSS injection,
yet Checkmarx is not able to link the different elements that are :
- the `@Valid` (SanitizeController.class)
- the `@Sanitize` (DemoForm.class)
- the `StringEscapeUtils.escapeHtml4(cValue);` (SanitizeValidator.class)

# Use 
 
- Start XssSanitizerApplication.class 
- run in cmd : 

```shell script
curl --location --request POST 'http://localhost:8080/sanitize' \
--header 'Content-Type: application/json' \
--data-raw '{
    "stringField" : "<script>console.log('\''toto'\'');</script>",
    "listStringField" : [
        "clearString",
        "<xml>tag</xml>"
    ],
    "setStringField" : [
        "clearString2",
        "clearString2",
        "<xml>tag2</xml>"
    ]
}'
```

the result should be :
```json
{
    "stringField": "&lt;script&gt;console.log('toto');&lt;/script&gt;",
    "listStringField": [
        "clearString",
        "&lt;xml&gt;tag&lt;/xml&gt;"
    ],
    "setStringField": [
        "&lt;xml&gt;tag2&lt;/xml&gt;",
        "clearString2"
    ]
}
```
