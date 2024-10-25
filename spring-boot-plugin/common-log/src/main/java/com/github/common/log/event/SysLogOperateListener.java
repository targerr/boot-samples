/*
 *  Copyright (c) 2019-2020, somowhere (somewhere0813@gmail.com).
 *  <p>
 *  Licensed under the GNU Lesser General Public License 3.0 (the "License");
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

package com.github.common.log.event;


import com.github.common.log.pojo.LogOperateDo;
import com.github.common.log.service.RemoteLogOperateService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;


/**
 * @author somowhere
 * 异步监听日志事件
 */
@Slf4j
@AllArgsConstructor
public class SysLogOperateListener {
    private final RemoteLogOperateService remoteLogOperateService;

    @Async
    @EventListener(SysLogOperateEvent.class)
    public void saveSysLog(SysLogOperateEvent event) {
        if (log.isTraceEnabled()) {
            log.trace("{}", event);
        }
        LogOperateDo logOperate = (LogOperateDo) event.getSource();
        logOperate.setIpLocation(logOperate.getIpAddress());
        remoteLogOperateService.save(logOperate);
    }
}
