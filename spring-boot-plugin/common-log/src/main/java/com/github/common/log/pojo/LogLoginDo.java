/*
 *  Copyright (c) 2019-2023  <a href="https://github.com/somowhere/albedo">Albedo</a>, somewhere (somewhere0813@gmail.com).
 *  <p>
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 * https://www.gnu.org/licenses/lgpl.html
 *  <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.common.log.pojo;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 登录日志管理Entity 登录日志
 *
 * @author admin
 * @version 2021-11-30 10:15:00
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class LogLoginDo implements Serializable {

    /**
     * F_TITLE title  :  日志标题
     */
    public static final String F_TITLE = "title";
    /**
     * F_USERID user_id  :  用户ID
     */
    public static final String F_USERID = "userId";
    /**
     * F_USERNAME username  :  用户名
     */
    public static final String F_USERNAME = "username";
    /**
     * F_IPADDRESS ip_address  :  IP地址
     */
    public static final String F_IPADDRESS = "ipAddress";
    /**
     * F_IPLOCATION ip_location  :  登录地点
     */
    public static final String F_IPLOCATION = "ipLocation";
    /**
     * F_BROWSER browser  :  浏览器类型
     */
    public static final String F_BROWSER = "browser";
    /**
     * F_OS os  :  操作系统
     */
    public static final String F_OS = "os";
    /**
     * F_USERAGENT user_agent  :  用户代理
     */
    public static final String F_USERAGENT = "userAgent";
    /**
     * F_REQUESTURI request_uri  :  请求URI
     */
    public static final String F_REQUESTURI = "requestUri";
    /**
     * F_EXECUTETIME execute_time  :  执行时间
     */
    public static final String F_EXECUTETIME = "executeTime";
    private static final long serialVersionUID = 1L;
    protected Long createdBy;

    protected LocalDateTime createdDate;
    /*** 备注 */

    protected String description;
    /**
     * 编号
     */

    private Long id;
    /**
     * title 日志标题
     */

    private String title;
    /**
     * userId 用户ID
     */

    private Long userId;
    /**
     * username 用户名
     */

    private String username;
    /**
     * nickname 登录人昵称
     */

    private String nickname;
    /**
     * ipAddress IP地址
     */
    private String ipAddress;
    /**
     * ipLocation 登录地点
     */

    private String ipLocation;
    /**
     * browser 浏览器类型
     */
    private String browser;
    /**
     * os 操作系统
     */

    private String os;
    /**
     * userAgent 用户代理
     */

    private String userAgent;
    /**
     * requestUri 请求URI
     */

    private String requestUri;
    /**
     * 操作提交的数据
     */

    private String params;
    /**
     * loginDate 登录日期
     */

    private String loginDate;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
