import com.virajprakash.sqlapi.MySQL;
import com.virajprakash.sqlapi.MySQLResponse;

/*************************************************************************
 *
 * J&M CONFIDENTIAL - @author Viraj Prakash - 5/26/17 | 11:27 AM
 * __________________
 *
 *  [2017] J&M Plugin Development 
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of J&M Plugin Development and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to J&M Plugin Development
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from J&M Plugin Development.
 */
public class Test {

	public void test() {
		MySQL database = new MySQL(null, null, null, null, null);
		String count = database.count("Accounts", "Email");
		MySQLResponse response = database.executeQuery(count, "me@virajprakash.com");
		int result = database.getCount(response);
		response.closeAll();
	}
}
