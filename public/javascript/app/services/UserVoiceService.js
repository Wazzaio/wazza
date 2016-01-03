/*
 * Wazza
 * https://github.com/Wazzaio/wazza
 * Copyright (C) 2013-2015  Duarte Barbosa, João Vazão Vasques
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

'use strict'

service.factory('UserVoiceService', ['ApplicationStateService', function (ApplicationStateService) {
	var service = {};
    
  service.bootstrap = function(){
    // Set colors
    UserVoice.push(['set', {
      accent_color: '#448dd6',
      trigger_color: 'white',
        trigger_background_color: '#e2753a'
    }]);
  
    // Add default trigger to the bottom-right corner of the window:
      UserVoice.push(['addTrigger', { mode: 'contact', trigger_position: 'bottom-right' }]);
  
      // Autoprompt for Satisfaction and SmartVote (only displayed under certain conditions)
      UserVoice.push(['autoprompt', {}]);
  };

  service.identifyUser = function(){
    var userInfo = ApplicationStateService.getUserInfo();
    var _email = userInfo.email
    var _name = userInfo.name;
    var company = ApplicationStateService.getCompanyName();
    UserVoice.push(['identify', {
      email: _email,
      name: _name,
      id: company
        //type:       'Owner', // Optional: segment your users by type
        //account: {
        //  id:           123, // Optional: associate multiple users with a single account
        //  name:         'Acme, Co.', // Account name
        //  created_at:   1364406966, // Unix timestamp for the date the account was created
        //  monthly_rate: 9.99, // Decimal; monthly rate of the account
        //  ltv:          1495.00, // Decimal; lifetime value of the account
        //  plan:         'Enhanced' // Plan name for the account
        //}
    }]);
  };
  
	return service;
}]);

