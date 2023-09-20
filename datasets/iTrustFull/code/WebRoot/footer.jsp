				</div>
				<div class="rightBorder" id="r"></div>
			</div>
			<div style="clear: both;">
			</div>
		</div>

	
		<div class="iTrustFooter" style="padding-left: 200px;">
			<div style="float: left; width: 48%; margin-left: 25px;">
<%
			if( ! "true".equals(System.getProperty("itrust.production") ) ) { 
%>
				  <a class="iTrustTestNavlink" href="/iTrust/util/andystestutil.jsp">Test Data Generator</a>
				| <a class="iTrustTestNavlink" href="/iTrust/util/transactionLog.jsp">Transaction Log</a>
				| <a class="iTrustTestNavlink" href="/iTrust/util/displayDatabase.jsp">Display Database</a>
				| <a class="iTrustTestNavlink" href="/iTrust/util/blackbox/blackbox.jsp">Black Box Test Plan</a>
				| <a class="iTrustTestNavlink" href="/iTrust/util/showFakeEmails.jsp">Show Fake Emails</a>
<%
			}
%>
			</div>
			<div style="float: right; width: 48%; text-align: right; margin-right: 10px;">
				  <a class="iTrustNavlink" href="mailto:nobody@itrust.com">Contact</a>
				| <a class="iTrustNavlink" href="/iTrust/privacyPolicy.jsp">Privacy Policy</a>
				| <a class="iTrustNavlink" href="http://agile.csc.ncsu.edu/iTrust/">iTrust v10.0</a>
			</div>
			<div style="clear: both;">
			</div>
		</div>

	</body>
</html>
