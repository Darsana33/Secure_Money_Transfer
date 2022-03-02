from public import *
from database import *

admin=Blueprint('admin',__name__)


@admin.route('/adminhome')
def adminhome():
	return render_template("adminhome.html")


@admin.route('/admin_view_merchant')
def admin_view_merchant():
	data={}
	q="select * from merchant inner join login using(login_id)"
	res=select(q)
	data['merchant']=res

	if 'action' in request.args:
		action=request.args['action']
		id=request.args['id']
	else:
		action=None

	if action=="accept":
		q="update login set usertype='merchant' where login_id='%s'"%(id)
		update(q)
		return redirect(url_for('admin.admin_view_merchant'))

	if action=="reject":
		q="update login set usertype='block' where login_id='%s'"%(id)
		update(q)
		return redirect(url_for('admin.admin_view_merchant'))
	return render_template("admin_view_merchant.html",data=data)

@admin.route('/admin_view_user')
def admin_view_user():
	data={}
	q="select * from user"
	res=select(q)
	data['user']=res
	return render_template("admin_view_user.html",data=data)

@admin.route('/admin_send_notification',methods=['get','post'])
def admin_send_notification():
	if 'send' in request.form:
		notification=request.form['noti']
		q="insert into notification values(null,'%s',now())"%(notification)
		insert(q)
	return render_template("admin_send_notification.html")

@admin.route('/admin_view_merchantcomplaints')
def admin_view_merchantcomplaints():
	data={}
	q="select * from complaint inner join merchant on(merchant.login_id=complaint.sender_id) where type='merchant'"
	print(q)
	res=select(q)
	data['merchantcomplaint']=res
	return render_template("admin_view_merchantcomplaints.html",data=data)

@admin.route('/admin_view_usercomplaints')
def admin_view_usercomplaints():
	data={}
	q="select * from complaint inner join user on(user.login_id=complaint.sender_id) where type='user'"
	print(q)
	res=select(q)
	data['usercomplaint']=res
	return render_template("admin_view_usercomplaints.html",data=data)

@admin.route('/admin_send_userreply',methods=['get','post'])
def admin_send_userreply():
	cid=request.args['cid']
	if 'send' in request.form:
		userreply=request.form['ureply']
		q="update complaint set reply='%s' where complaint_id='%s' "%(userreply,cid)
		update(q)
		return redirect(url_for('admin.admin_view_usercomplaints'))
	return render_template("admin_send_userreply.html")

@admin.route('/admin_send_merchantreply',methods=['get','post'])
def admin_send_merchantreply():
	cid=request.args['cid']
	if 'send' in request.form:
		merchantreply=request.form['mreply']
		q="update complaint set reply='%s' where complaint_id='%s' "%(merchantreply,cid)
		update(q)
		return redirect(url_for('admin.admin_view_merchantcomplaints'))
	return render_template("admin_send_merchantreply.html")

