## Testing

#### Unit Tests

Using sbt. Tests are in `/src/test/java` directory.

To run all the tests:

`sbt clean test`
##### Notes

The unit tests generate test outputs, saved in `/test-output`.


#### Code coverage

Using jacoco.
To find the code coverage:
`sbt clean jacoco:cover`
##### Notes

The code coverage report is generated in `/target/jacoco/html/index.html`

