# Json Views for Versioning
This is an example of using JSON Views for versioning POJOs.

## Controllers
### Accept Controller
The ItemController uses the `Accept` header to determine
the response version.

### URI Controller
The ItemV[123]Controller uses URI versioning to determine
the response version.

Both are provided for comparison.