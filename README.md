## **FoodRequest Endpoints**

### **POST /api/foodRequests/create**

**Expected Input Parameters**:

- `clientId` (Integer): The ID of the client making the request.
- `accountId` (Integer): The ID of the account making the request.
- `listingId` (Integer): The ID of the food listing being requested.
- `quantityRequested` (Integer): The quantity of food being requested.

**Expected Output**:

- A `FoodRequest` object containing details of the newly created request.

This endpoint creates a new food request for the specified account, client, and food listing with
the requested quantity. This is a key step in the process of requesting food from the system.

**Upon Success**:

- **HTTP 201** Status Code is returned along with the created `FoodRequest` in the response body.

**Upon Failure**:

- **HTTP 404** Status Code is returned if any of the specified IDs (clientId, accountId, listingId)
  do not exist.

### **GET /api/foodRequests/get**

**Expected Input Parameters**:

- `requestId` (Integer): The ID of the food request to retrieve.

**Expected Output**:

- A JSON object containing the details of the food request, including:
    - `request_id`: The ID of the food request.
    - `client_id`: The ID of the client making the request.
    - `account_id`: The ID of the account making the request.
    - `listing_id`: The ID of the food listing being requested.
    - `quantity_requested`: The quantity of food requested.
    - `request_time`: The time the request was made.

This endpoint retrieves the details of a food request by its request ID.

**Upon Success**:

- **HTTP 200** Status Code is returned along with the food request details in the response body.

**Upon Failure**:

- **HTTP 404** Status Code is returned if the specified `requestId` does not exist.
- **HTTP 400** Status Code is returned if an error occurs while processing the request.

### **PUT /api/foodRequests/update**

**Expected Input Parameters**:

- `requestId` (Integer): The ID of the food request to update.
- `quantityRequested` (Integer): The new quantity of food requested.

**Expected Output**:

- A message indicating the result of the update operation.

This endpoint updates the quantity of food requested in an existing food request.

**Upon Success**:

- **HTTP 200** Status Code is returned with the message `"Updated Successfully."` in the response
  body.

**Upon Failure**:

- **HTTP 404** Status Code is returned if the specified `requestId` does not exist.

### **Operational Guidelines**

- **Order of API Calls**: The `/create` endpoint should be called before `/get` or `/update` to
  ensure a food request exists.
- **Error Handling**: Proper status codes like `404 Not Found` or `400 Bad Request` will be returned
  for invalid requests or errors.
- **Dependencies**: Ensure valid `clientId`, `accountId`, and `listingId` exist before creating a
  request.

--- 

## **ClientProfile Endpoints**

### **POST /api/clientProfiles/create**

**Expected Input Parameters**: N/A  
**Expected Output**: A `ClientProfile` object containing the `client_id` and other details.

This endpoint creates a new client profile. Each client is registered with a unique ID, which can be
used in other operations.

**Upon Success**:

- **HTTP 201** Status Code is returned along with the newly created `ClientProfile` in the response
  body.

**Upon Failure**:

- **HTTP 500** Status Code is returned in case of any unexpected errors during profile creation.

### **GET /api/clientProfiles/get**

**Expected Input Parameters**:

- `clientId` (Integer): The ID of the client profile to retrieve.

**Expected Output**: A JSON object containing:

- `client_id`: The ID of the client.

This endpoint retrieves the details of a client profile by the provided client ID.

**Upon Success**:

- **HTTP 200** Status Code is returned along with the client profile details in the response body.

**Upon Failure**:

- **HTTP 404** Status Code is returned if the specified `clientId` does not exist.

### **Operational Guidelines**

- **Order of API Calls**: You should call the `/create` endpoint to create a client profile before
  trying to retrieve it using `/get`.
- **Error Handling**: If a `clientId` does not exist, the service will return a `404 Not Found`
  status code. Ensure that the client ID is valid before making a request to `/get`.
- **Dependencies**: The client profile created by `/create` can be used by other services that
  require a client profile (e.g., creating food requests).

---

## **AccountProfile Endpoints**

### **POST /api/accountProfiles/create**

**Expected Input Parameters**:

- `clientId` (Integer): The ID of the client to associate with the account profile.
- `accountType` (Enum: PROVIDER, RECIPIENT): The type of account being created (e.g., PROVIDER or
  RECIPIENT).
- `phoneNumber` (String): The phone number associated with the account.
- `name` (String): The name of the account holder.

**Expected Output**:

- A JSON object representing the newly created `AccountProfile`, including:
    - `accountId`: The ID of the account profile.
    - `accountType`: The type of account (e.g., PROVIDER or RECIPIENT).
    - `phoneNumber`: The phone number associated with the account.
    - `name`: The name of the account holder.

This endpoint creates a new account profile for a specific client. Each client can have multiple
account profiles, and this operation allows for the creation of those accounts with the specified
type, phone number, and name.

**Upon Success**:

- **HTTP 201** Status Code is returned along with the created `AccountProfile` object in the
  response body.

**Upon Failure**:

- **HTTP 404** Status Code is returned if the `clientId` does not exist in the system.

### **GET /api/accountProfiles/get**

**Expected Input Parameters**:

- `accountId` (Integer): The ID of the account profile to retrieve.

**Expected Output**:

- A JSON object containing:
    - `account_id`: The ID of the account profile.
    - `name`: The name of the account holder.

This endpoint retrieves the details of an existing account profile based on the provided account ID.
The retrieved information includes the account holder's name and the account ID.

**Upon Success**:

- **HTTP 200** Status Code is returned along with the account profile details in the response body.

**Upon Failure**:

- **HTTP 404** Status Code is returned if the specified `accountId` does not exist.

### **Operational Guidelines**

- **Order of API Calls**: The `/create` endpoint must be called before retrieving an account profile
  using `/get` to ensure the account exists.
- **Error Handling**: If an invalid `clientId` or `accountId` is provided, the service will return a
  `404 Not Found` status code. Ensure that valid IDs are used in the requests.

---
