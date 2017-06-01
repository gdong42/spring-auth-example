# spring-auth-example
Demonstrates multiple authentication providers using spring security.

## What is this example about?

This demo shows how to quickly setup an auth server of OAuth 2.0 using spring boot
and spring-oauth-security.

It uses username/password authentication to exchange for a JWT Access Token / Refresh
Token.

In addition, it demonstrates how to use client_id to differentiate two
authentication providers that could verify username/password in completely different
stores. This is useful when you want to use a same auth server for multiple user
stores - perhaps different applications with isolated user systems.

## How to run the demo?

* simply start the server using following command

```
./gradlew bootRun
```

it will start the server on port 9090.

* Request Access Token & Refresh Token

The example code hardcoded a user `gan/foo` for client_id `foo_app`, and `dong/bar`
for client_id 'bar_app'; try curl the server using above combinations.

You should be able to see `gan/foo` can only be authenticated with client_id `foo_app`,
and `dong/bar` can only be authenticated with client_id `bar_app`.

e.g.

```
curl -X POST -H "Authorization: Basic YmFyX2FwcDo=" -H "Content-Type: application/x-www-form-urlencoded" -d 'grant_type=password&username=dong&password=bar' "http://localhost:9090/oauth/token"
```

Note above Basic auth header `Basic YmFyX2FwcDo=` is encoded from client_id `bar_app` with empty password.
In other words, it is Base64 encoded from string `bar_app:`, notice the trailing colon;

The response json would look like

```
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE0ODUzMTkyOTIsInVzZXJfbmFtZSI6ImRvbmciLCJhdXRob3JpdGllcyI6WyJCQVJfUkVBRCIsIkJBUl9XUklURSJdLCJqdGkiOiI4OTRiZGVmNi1jZjk1LTQ2YTYtYjE0MC0wOTNjZDdmMGVlZWIiLCJjbGllbnRfaWQiOiJiYXJfYXBwIiwic2NvcGUiOlsiQkFSIl19.rRfdNOBQ-lDXRJTgtaEe75UOUkd8PM6lWpapqYWu19QR1c9ZM21n-rhWtsc-zonFh1a-uxO-9eh1B1d7hx9VrYZlFkcJFtvSGAS5e7bEaWkCTSxcP6DLDa1bRjV_5UQVEPilLQrznvptB2FVxe11p9Z8RbN1xk4SqHvwOVaDCW0PXvQFEjerX9oRFWufh9MYS7LYaWvKXagwl3BsQEEFVTI0zoh-A2_hlYXIlclYHHCDAqaVQ4rX6vreYyTUzfcg3PCXrGCnBgkGJTID_p06uJDaji4DFjheQN1lTDm0muACEQ6Ie3nZHn-zVZevLJYC_uAaoiVNEvNXxX4sHdRo8A",
  "token_type": "bearer",
  "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJkb25nIiwic2NvcGUiOlsiQkFSIl0sImF0aSI6Ijg5NGJkZWY2LWNmOTUtNDZhNi1iMTQwLTA5M2NkN2YwZWVlYiIsImV4cCI6MTQ4Nzg2ODA5MiwiYXV0aG9yaXRpZXMiOlsiQkFSX1JFQUQiLCJCQVJfV1JJVEUiXSwianRpIjoiNjczM2ZiMzItMzJkOS00ZjRlLWJhMDYtNzFmOTIzZDYwNDZmIiwiY2xpZW50X2lkIjoiYmFyX2FwcCJ9.pbnSeWQQ1XEZFrrpFEHd_BVFHh67XZv7TEajgHHL3oevKAOoWdq1iAxaXE5ImSbKAB2Z0NLUPy8HmCzZc2A5r853PGM_3MsRk2_ggdgKludTd9x8dZjs4eph1Vw6JM4x3K1KA72nodok7L0yMLUDiCiEMCr6lXVD5NLTelxW9ZcI7dZQhfGfA-ipdwbLTYFY2ZgQ7dYtaGtfgW1H7AYfo89l-AQHpCjE2MQiAsm_L5D1L41sKYM8lAcMAQ5X5YtXRYmLGGoET2wfdtC_0v57SqzgvojbzHOkOhd6tcr7WHI3RxZ9Kcnx_3Jc31JYLFxhCi7CtxT7ShzFt1UXQk9Eiw",
  "expires_in": 43199,
  "scope": "BAR",
  "jti": "894bdef6-cf95-46a6-b140-093cd7f0eeeb"
}
```

* Refresh tokens with Refresh Token

Request the server with a refresh_token, which is saved from above step, and grant type 'refresh_token', it will
response with a pair of new access_token/refresh_token. E.g.

```
curl -X POST -H "Authorization: Basic YmFyX2FwcDo=" -H "Content-Type: application/x-www-form-urlencoded" -d 'grant_type=refresh_token&refresh_token=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJkb25nIiwic2NvcGUiOlsiQkFSIl0sImF0aSI6Ijg5NGJkZWY2LWNmOTUtNDZhNi1iMTQwLTA5M2NkN2YwZWVlYiIsImV4cCI6MTQ4Nzg2ODA5MiwiYXV0aG9yaXRpZXMiOlsiQkFSX1JFQUQiLCJCQVJfV1JJVEUiXSwianRpIjoiNjczM2ZiMzItMzJkOS00ZjRlLWJhMDYtNzFmOTIzZDYwNDZmIiwiY2xpZW50X2lkIjoiYmFyX2FwcCJ9.pbnSeWQQ1XEZFrrpFEHd_BVFHh67XZv7TEajgHHL3oevKAOoWdq1iAxaXE5ImSbKAB2Z0NLUPy8HmCzZc2A5r853PGM_3MsRk2_ggdgKludTd9x8dZjs4eph1Vw6JM4x3K1KA72nodok7L0yMLUDiCiEMCr6lXVD5NLTelxW9ZcI7dZQhfGfA-ipdwbLTYFY2ZgQ7dYtaGtfgW1H7AYfo89l-AQHpCjE2MQiAsm_L5D1L41sKYM8lAcMAQ5X5YtXRYmLGGoET2wfdtC_0v57SqzgvojbzHOkOhd6tcr7WHI3RxZ9Kcnx_3Jc31JYLFxhCi7CtxT7ShzFt1UXQk9Eiw' "http://localhost:9090/oauth/token"
```

generates

```
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE0ODUzMTkzODMsInVzZXJfbmFtZSI6ImRvbmciLCJhdXRob3JpdGllcyI6WyJCQVJfUkVBRCIsIkJBUl9XUklURSJdLCJqdGkiOiIyMDA1NDVmYi1hZWIwLTQzMjUtOTNhNS00NTFkMGViYzVmM2YiLCJjbGllbnRfaWQiOiJiYXJfYXBwIiwic2NvcGUiOlsiQkFSIl19.f3XA46ZWu3468ri_RJa9vvsAT9sCpx_O-ZCTk0-78Gq56I-vubHlZKatcFzLnAF72I7NhWZNujrp5kALzwYMvbwqzYuDl9wWNGu6Wrozsg4N6b2G9Q5SUe1AhsC25wY4QI0okcHzpY_yrkQC82XXxf0X_llN452j3a2Z9l2PUUw-PSQIlI4lSZfHPR8U8c3RdQ4kLPofUa2KIUlmuwwENxxjgehitWCTDYKcctPIVLG-h2QydCle_8xQ6AmhvhSbNccSd2QI3z7nGiQleWFgH2RFDYUwn43JWe9V9c1W-kVnplO0TRK4ZQeDk1B9ZUCoM0VCHTKzI3xty0NUuHsvUw",
  "token_type": "bearer",
  "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJkb25nIiwic2NvcGUiOlsiQkFSIl0sImF0aSI6IjIwMDU0NWZiLWFlYjAtNDMyNS05M2E1LTQ1MWQwZWJjNWYzZiIsImV4cCI6MTQ4Nzg2ODA5MiwiYXV0aG9yaXRpZXMiOlsiQkFSX1JFQUQiLCJCQVJfV1JJVEUiXSwianRpIjoiNjczM2ZiMzItMzJkOS00ZjRlLWJhMDYtNzFmOTIzZDYwNDZmIiwiY2xpZW50X2lkIjoiYmFyX2FwcCJ9.X2w5U0gLqOiibr1aGceRcykm-lz2N8X3oN_YG1TJBOwFESEJC_oyqsQBv1o3DyXtJ3axM_ZPqf-2g9gKRMNJXjHhNhceHJL1ZZgVKoIDS53SHk71MUV7-29IYNlDv4QJKILdH1-PO84oXMDbJMgoxAl_4vucKmANkg3NSkzVV91rzp8vXpm_dFMMAT36UFhdpgMc1ZlXIeWwBso_yQywr4CVD3xhcbHD3Y6YjH-QFo91TrBQFE_CllxG5fgLtBlj307BJZ6nx08-XXpP8_M5eKWofUv3SfoTLV5I2ekUBzNKcpnlflw8tlUl4I0Ixt8uAijSCSZLbKVq9XPDKDi_5w",
  "expires_in": 43199,
  "scope": "BAR",
  "jti": "200545fb-aeb0-4325-93a5-451d0ebc5f3f"
}
```
