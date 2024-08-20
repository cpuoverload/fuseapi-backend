use fuseapi;

INSERT INTO `api_detail` (`api_name`,
                          `description`,
                          `method`,
                          `api_path`,
                          `request_header`,
                          `query_params`,
                          `body_params`,
                          `response_body`,
                          `response_error`,
                          `status`,
                          `userId`)
VALUES ('Get User Info',
        '获取用户信息',
        'GET',
        '/api/v1/user/info',
        '{
          "Authorization": "Bearer <token>"
        }',
        '[
          {
            "name": "userId",
            "type": "int",
            "description": "用户 ID",
            "required": true
          }
        ]',
        NULL,
        '{
          "userId": 1,
          "userName": "John Doe",
          "email": "john.doe@example.com"
        }',
        '[
          {
            "http_code": 404,
            "msg": "Invalid user ID"
          }
        ]',
        1,
        123);

INSERT INTO `api_detail` (`api_name`,
                          `description`,
                          `method`,
                          `api_path`,
                          `request_header`,
                          `query_params`,
                          `body_params`,
                          `response_body`,
                          `response_error`,
                          `status`,
                          `userId`)
VALUES ('Create User',
        '创建新用户',
        'POST',
        '/api/v1/user/create',
        NULL,
        NULL,
        '[
          {
            "name": "userName",
            "type": "string",
            "description": "用户名",
            "required": true
          },
          {
            "name": "email",
            "type": "string",
            "description": "用户邮箱",
            "required": true
          },
          {
            "name": "password",
            "type": "string",
            "description": "用户密码",
            "required": true
          }
        ]',
        '{
          "userId": 1,
          "userName": "John Doe",
          "email": "john.doe@example.com"
        }',
        '[
          {
            "http_code": 400,
            "msg": "Missing required fields"
          }
        ]',
        1,
        123);

INSERT INTO `api_detail` (`api_name`,
                          `description`,
                          `method`,
                          `api_path`,
                          `request_header`,
                          `query_params`,
                          `body_params`,
                          `response_body`,
                          `response_error`,
                          `status`,
                          `userId`)
VALUES ('Update User',
        '更新用户信息',
        'PUT',
        '/api/v1/user/update',
        '{
          "Authorization": "Bearer <token>"
        }',
        NULL,
        '[
          {
            "name": "userId",
            "type": "int",
            "description": "用户 ID",
            "required": true
          },
          {
            "name": "userName",
            "type": "string",
            "description": "用户名",
            "required": false
          },
          {
            "name": "email",
            "type": "string",
            "description": "用户邮箱",
            "required": false
          }
        ]',
        '{
          "status": "success",
          "message": "User updated successfully"
        }',
        '[
          {
            "http_code": 404,
            "msg": "User not found"
          }
        ]',
        1,
        123);

INSERT INTO `api_detail` (`api_name`,
                          `description`,
                          `method`,
                          `api_path`,
                          `request_header`,
                          `query_params`,
                          `body_params`,
                          `response_body`,
                          `response_error`,
                          `status`,
                          `userId`)
VALUES ('Delete User',
        '删除用户',
        'DELETE',
        '/api/v1/user/delete',
        '{
          "Authorization": "Bearer <token>"
        }',
        '[
          {
            "name": "userId",
            "type": "int",
            "description": "用户 ID",
            "required": true
          }
        ]',
        NULL,
        '{
          "status": "success",
          "message": "User deleted successfully"
        }',
        '[
          {
            "http_code": 404,
            "msg": "User not found"
          }
        ]',
        1,
        123);
