from flask import *
from database import *
import demjson

api=Blueprint('api',__name__)

@api.route('/login')
def login():
	data={}
	username=request.args['username']
	password=request.args['password']
	q="select * from login where username='%s' and password='%s'"%(username,password)
	res=select(q)
	if res:
		if res[0]['usertype']=="merchant":
			q1="select *,merchant_id as uid,merchantname as uname  from merchant where login_id='%s'"%(res[0]['login_id'])
			res1=select(q1)
		if res[0]['usertype']=="user":
			q1="select *,user_id as uid,concat(firstname,' ',lastname) as uname from user where login_id='%s'"%(res[0]['login_id'])
			res1=select(q1)
		data['uname']=res1[0]['uname']
		data['uid']=res1[0]['uid']
		data['status']="Success"
		data['data']=res
	else:
		data['status']="Failed"
	return demjson.encode(data)


@api.route('/merchantreg')
def merchantreg():
	data={}
	name=request.args['name']
	place=request.args['place']
	phone=request.args['phone']
	email=request.args['email']
	username=request.args['uname']
	password=request.args['pass']
	q="insert into login values(null,'%s','%s','merchant')"%(username,password)
	id=insert(q)
	q="insert into merchant values(null,'%s','%s','%s','%s','%s')" %(id,name,place,phone,email)
	insert(q)
	q="insert into wallet values(null,'%s','0')" %(id)
	insert(q)
	print(q)
	data['status']="success"
	return demjson.encode(data)

@api.route('/userreg')
def userreg():
	data={}
	fname=request.args['fname']
	lname=request.args['lname']
	house=request.args['house']
	place=request.args['place']
	phone=request.args['phone']
	email=request.args['email']
	gender=request.args['gender']
	username=request.args['uname']
	password=request.args['pass']
	q="insert into login values(null,'%s','%s','user')"%(username,password)
	id=insert(q)
	q="insert into user values(null,'%s','%s','%s','%s','%s','%s','%s','%s')" %(id,fname,lname,house,place,phone,email,gender)
	insert(q)
	q="insert into wallet values(null,'%s','0')" %(id)
	insert(q)
	print(q)
	data['status']="success"
	return demjson.encode(data)

@api.route('/wallet')
def wallet():
	data={}
	lid=request.args['lid']
	amount=request.args['amount']
	q="select * from wallet where login_id='%s'"%(lid)
	res=select(q)
	if res:
		q="update wallet set amount=amount+'%s' where login_id='%s'"%(amount,lid)
		update(q)
	else:
		q="insert into wallet values(null,'%s','%s')" %(lid,amount)
		insert(q)
	q="insert into transaction values(null,'%s','%s','credit',curdate())" %(lid,amount)
	insert(q)
	print(q)
	data['status']="success"
	data['method']="wallet"
	return demjson.encode(data)

@api.route('/viewwallet')
def viewwallet():
	data={}
	lid=request.args['lid']
	q="select * from transaction where login_id='%s' order by transaction_id desc"%(lid)
	res=select(q)
	q="select * from wallet where login_id='%s'"%(lid)
	res1=select(q)
	if res1:
		data['status']="success"
		data['data']=res
		data['tamount']=res1[0]['amount']
	else:
		data['status']="failed"
	data['method']="viewwallet"
	return demjson.encode(data)


@api.route('/sendcomplaints')
def sendcomplaints():
	data={}
	lid=request.args['lid']
	complaint=request.args['complaint']
	types=request.args['type']
	q="insert into complaint values(null,'%s','%s','pending',curdate(),'%s')"%(lid,complaint,types)
	insert(q)
	data['status']="success"
	data['method']="sendcomplaints"
	return demjson.encode(data)


@api.route('/viewcomplaints')
def viewcomplaints():
	data={}
	lid=request.args['lid']
	q="select * from complaint where sender_id='%s'"%(lid)
	res=select(q)
	if res:
		data['status']="success"
		data['data']=res
	else:
		data['status']="failed"
	data['method']="viewcomplaints"
	return demjson.encode(data)

@api.route('/viewnotification')
def viewnotification():
	data={}
	q="select * from notification"
	res=select(q)
	if res:
		data['status']="success"
		data['data']=res
	else:
		data['status']="failed"
	return demjson.encode(data)

@api.route('/usermakepayment')
def usermakepayment():
	data={}
	lid=request.args['lid']
	mid=request.args['mid']
	amount=request.args['amount']
	q="insert into transaction values(null,'%s','%s','debit',curdate())" %(lid,amount)
	insert(q)
	q="insert into transaction values(null,'%s','%s','credit',curdate())" %(mid,amount)
	insert(q)
	q="insert into transfer values(null,'%s','%s','%s',curdate())" %(lid,mid,amount)
	insert(q)
	q="update wallet set amount=amount-'%s' where login_id='%s'"%(amount,lid)
	update(q)
	q="update wallet set amount=amount+'%s' where login_id='%s'"%(amount,mid)
	update(q)
	data['status']="success"
	data['method']="usermakepayment"

	return demjson.encode(data)


@api.route('/userviewbalance')
def userviewbalance():
	data={}
	lid=request.args['lid']
	amt=request.args['amt']
	q="select * from wallet where amount>500 and login_id='%s'" %(lid)
	res=select(q)
	print(res)
	if res:
		amts=int(res[0]['amount'])-int(amt)
		if amts>500:
			data['status']="success"
			data['data']=res
		else:
			data['status']="lm"
	else:
		data['status']="lm"
	data['method']="userviewbalance"
	return demjson.encode(data)


@api.route('/viewpayment')
def viewpayment():
	data={}
	lid=request.args['lid']
	q="""SELECT t.`transfer_id`,CONCAT(u.`firstname`,' ',u.`lastname`) AS sname,m.`merchantname` AS rname,t.`amount`,t.`date` FROM transfer t,merchant m,USER u WHERE (t.`sender_id`=u.`login_id` AND t.`receiver_id`=m.`login_id` and  t.sender_id='%s') or (t.`sender_id`=u.`login_id` AND t.`receiver_id`=m.`login_id` and  t.receiver_id='%s') or (t.`sender_id`=m.`login_id` AND t.`receiver_id`=u.`login_id` and  t.sender_id='%s') or (t.`sender_id`=m.`login_id` AND t.`receiver_id`=u.`login_id` and  t.receiver_id='%s')
	UNION
	SELECT t.`transfer_id`,CONCAT(u1.`firstname`,' ',u1.`lastname`) AS sname,CONCAT(u2.`firstname`,' ',u2.`lastname`) AS rname,t.`amount`,t.`date` FROM transfer t,USER u1,USER u2 WHERE (t.`sender_id`=u1.login_id AND t.receiver_id=u2.login_id and  t.sender_id='%s') or (t.`sender_id`=u1.login_id AND t.receiver_id=u2.login_id and  t.receiver_id='%s') or (t.`sender_id`=u2.login_id AND t.receiver_id=u1.login_id and  t.sender_id='%s') or (t.`sender_id`=u2.login_id AND t.receiver_id=u1.login_id and  t.receiver_id='%s')"""%(lid,lid,lid,lid,lid,lid,lid,lid)
	print(q)
	res=select(q)
	if res:
		data['status']="success"
		data['data']=res
	else:
		data['status']="failed"
	return demjson.encode(data)
@api.route('/viewpayments')
def viewpayments():
	data={}
	lid=request.args['lid']
	search=request.args['search']
	q="""SELECT t.`transfer_id`,CONCAT(u.`firstname`,' ',u.`lastname`) AS sname,m.`merchantname` AS rname,t.`amount`,t.`date` FROM transfer t,merchant m,USER u WHERE (t.`sender_id`=u.`login_id` AND t.`receiver_id`=m.`login_id` and  t.sender_id='%s' and t.`date` = '%s') or (t.`sender_id`=u.`login_id` AND t.`receiver_id`=m.`login_id` and  t.receiver_id='%s'and t.`date` = '%s') or (t.`sender_id`=m.`login_id` AND t.`receiver_id`=u.`login_id` and  t.sender_id='%s' and t.`date` = '%s') or (t.`sender_id`=m.`login_id` AND t.`receiver_id`=u.`login_id` and  t.receiver_id='%s' and t.`date` = '%s')
	UNION
	SELECT t.`transfer_id`,CONCAT(u1.`firstname`,' ',u1.`lastname`) AS sname,CONCAT(u2.`firstname`,' ',u2.`lastname`) AS rname,t.`amount`,t.`date` FROM transfer t,USER u1,USER u2 WHERE (t.`sender_id`=u1.login_id AND t.receiver_id=u2.login_id and  t.sender_id='%s' and t.`date` = '%s') or (t.`sender_id`=u1.login_id AND t.receiver_id=u2.login_id and  t.receiver_id='%s' and t.`date` = '%s') or (t.`sender_id`=u2.login_id AND t.receiver_id=u1.login_id and  t.sender_id='%s' and t.`date` = '%s') or (t.`sender_id`=u2.login_id AND t.receiver_id=u1.login_id and  t.receiver_id='%s' and t.`date` = '%s')"""%(lid,search,lid,search,lid,search,lid,search,lid,search,lid,search,lid,search,lid,search)
	print(q)
	res=select(q)
	if res:
		data['status']="success"
		data['data']=res
	else:
		data['status']="failed"
	return demjson.encode(data)



@api.route('/searchsnumber')
def searchsnumber():
	data={}
	num=request.args['num']
	lid=request.args['lid']
	amt=request.args['amt']
	q="select * from user where phone='%s'" %(num)
	res=select(q)
	if res:
		q="insert into request values(null,'%s','%s','%s',curdate(),'pending')" %(lid,res[0]['login_id'],amt)
		insert(q)
	
		data['status']="success"
	else:
		data['status']="failed"
	data['method']="searchsnumber"

	return demjson.encode(data)


@api.route('/viewmoneyrequest')
def viewmoneyrequest():
	data={}
	lid=request.args['lid']
	q="""select request_id,concat(firstname,' ',lastname) as user,amount,date,status from request inner join user on request.sender_id=user.login_id where receiver_id='%s' and status='pending'
	union
	select request_id,merchantname as user,amount,date,status from request inner join merchant on request.sender_id=merchant.login_id where receiver_id='%s' and status='pending'""" %(lid,lid)
	res=select(q)
	print(res)
	if res:
		data['status']="success"
		data['data']=res
		
	else:
		data['status']="failed"
	data['method']="viewmoneyrequest"
	return demjson.encode(data)

@api.route('/usermakepaymentss')
def usermakepaymentss():
	data={}
	rid=request.args['rid']
	q="select * from request where request_id='%s'" %(rid)
	res=select(q)
	q="insert into transaction values(null,'%s','%s','debit',curdate())" %(res[0]['receiver_id'],res[0]['amount'])
	insert(q)
	q="insert into transaction values(null,'%s','%s','credit',curdate())" %(res[0]['sender_id'],res[0]['amount'])
	insert(q)
	q="insert into transfer values(null,'%s','%s','%s',curdate())" %(res[0]['sender_id'],res[0]['receiver_id'],res[0]['amount'])
	insert(q)
	q="update wallet set amount=amount-'%s' where login_id='%s'"%(res[0]['amount'],res[0]['receiver_id'])
	update(q)
	q="update wallet set amount=amount+'%s' where login_id='%s'"%(res[0]['amount'],res[0]['sender_id'])
	update(q)
	q="update request set status='paid' where request_id='%s'"%(rid)
	update(q)
	data['status']="success"
	data['method']="usermakepaymentss"

	return demjson.encode(data)