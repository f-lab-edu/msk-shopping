# import random
# from locust import task, FastHttpUser, stats
#
# stats.PERCENTILES_TO_CHART = [0.95, 0.99]
#
#
# class CouponIssueV1(FastHttpUser):
#     connection_timeout = 10.0
#     network_timeout = 10.0
#
#     @task
#     def issue(self):
#         payload = {
# #             "userId": random.randint(1, 10000000),
#             "userId": 12,
#             "couponPolicyId": 2
#         }
#         headers = {
#             "Authorization": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMiIsIm5pY2tuYW1lIjoiYWJjZEBuYXZlci5jb20iLCJpc0FkbWluIjoidHJ1ZSIsInVzZXJJZCI6MTIsImV4cCI6MTczMjcxNDI0MX0.7rQVY0EYdnTg8x5aqDjf7LGzcZX-eiKsRLhdQ_zvW7A"  # Add JWT token
# #             "Content-Type": "application/json"  # Optional: specify content type
#         }
#         with self.rest("POST", "/coupon/issue", json=payload):
#             pass

from locust import task, FastHttpUser, stats

class CouponIssueV1(FastHttpUser):
    connection_timeout = 10.0
    network_timeout = 10.0

    @task
    def issue(self):
        payload = {
            "userId": 9,
            "couponPolicyId": 2
        }
        headers = {
            "Authorization": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5Iiwibmlja25hbWUiOiJhYmNkQG5hdmVyLmNvbSIsImlzQWRtaW4iOiJ0cnVlIiwidXNlcklkIjo5LCJleHAiOjE3MzMzODIxMDV9.V-nbG1eAcHwvMwr1_j3SWNRa41u-sDXTBy5jyvRluW",
            "Content-Type": "application/json"  # Recommended to include this
        }
        response = self.client.post("/coupon/issue", json=payload, headers=headers)

        # Optional logging or assertion
        if response.status_code != 200:
            print(f"Request failed with status {response.status_code}: {response.text}")
