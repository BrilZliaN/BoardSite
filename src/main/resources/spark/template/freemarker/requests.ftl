<#ftl encoding="utf-8">
<!DOCTYPE html>
<html lang="ru">
   <head>
      <meta charset="utf-8">
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
      <meta http-equiv="X-UA-Compatible" content="IE=edge">
      <meta name="viewport" content="width=device-width, initial-scale=1">
      <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
      <meta name="description" content="">
      <meta name="author" content="">
      <link rel="icon" href="http://getbootstrap.com/favicon.ico">
      <title>${pagetitle} | ${projecttitle}</title>
      <!-- Bootstrap core CSS -->
      <link href="./assets/bootstrap.min.css" rel="stylesheet">
      <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">
      <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
      <link href="./assets/ie10-viewport-bug-workaround.css" rel="stylesheet">
      <!-- Custom styles for this template -->
      <link href="https://fonts.googleapis.com/css?family=PT+Sans" rel="stylesheet">
      <link href="./assets/offcanvas.css" rel="stylesheet">
      <link href="./assets/style.css" rel="stylesheet">
      <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
      <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
      <![endif]-->
   </head>
   <body>
      <nav class="navbar navbar-fixed-top navbar-inverse">
         <div class="container">
            <div class="navbar-header">
               <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
               <span class="sr-only">Открыть меню</span>
               <span class="icon-bar"></span>
               <span class="icon-bar"></span>
               <span class="icon-bar"></span>
               </button>
               <a class="navbar-brand" href="/">${projecttitle}</a>
            </div>
            <div id="navbar" class="collapse navbar-collapse">
               <ul class="nav navbar-nav">
                  <#list projectmenu as item>
                  <li <#if item.url == pageurl>class="active"</#if>><a href="/${item.url}">${item.title}</a></li>
                  </#list>
               </ul>
               <ul class="nav navbar-nav navbar-right">
                  <#if userloggedin>
                  <li><a href="/profile"><i class="fa fa-user"></i> ${username}</a></li>
                  <#if isPerformer>
                  <li><a href="/requests"><i class="fa fa-tasks"></i> Заказы</a></li>
                  </#if>
                  <#if useradmin>
                  <li><a href="/adminka"><i class="fa fa-cogs"></i> Админ-панель</a></li>
                  </#if>
                  <#else>
                  <li><a href="#" onclick="$('#login').modal();"><i class="fa fa-sign-in"></i> Вход</a></li>
                  <li><a href="/register">Регистрация</a></li>
                  </#if>
               </ul>
            </div>
            <!-- /.nav-collapse -->
         </div>
         <!-- /.container -->
      </nav>
      <!-- /.navbar -->
      <div class="container">
        <div class="col-md-6">
          <div class="panel panel-info">
              <div class="panel-heading">
                  <h3 class="panel-title">Последние заказы</h3>
              </div>
              <div class="panel-body">
               <#list requestlist as item>
                <div class="panel panel-default">
                  <div class="panel-body">
                    <div><b>Услуга: </b>${item.task_title}</div>
                    <div><b>Заказчик: </b>${item.customer_name}</div>
                    <div><b>E-mail: </b>${item.customer_email}</div>
                    <div><b>Телефон: </b>${item.customer_phone}</div>
                    <div><b>Дата заказа: </b>${item.request_datetime}</div>
                  </div>
                  <#if item.hasPerformer>
                    <div class="panel-footer"><b>Заказ взял: </b>${item.performer_name} (${item.performer_phone})</div>
                  <#else>
                    <div class="panel-footer"><a class="btn btn-primary btn-sm" href="/requests/assign/${item.request_id}">Взять заказ</a></div>
                  </#if>
                </div>
               </#list>
              </div>
          </div>
        </div>
        <div class="col-md-6">
          <div class="panel panel-info">
              <div class="panel-heading mid">
                  <div class="pull-right mid">
                      <a class="btn btn-success btn-xs" href="/requests/add"><i class="fa fa-plus" aria-hidden="true"></i> Добавить</a>
                  </div>
                  <h3 class="panel-title">Все услуги</h3>
              </div>
              <div class="panel-body">
			  <#list tasklist as item>
                <div class="panel panel-default">
                  <div class="panel-heading">
				    <div class="pull-right"><span class="label label-default"><b>${item.task_budget}</b></span> <!--<a class="btn btn-warning btn-xs" href="/requests/edit/${item.task_id}"><i class="fa fa-pencil" aria-hidden="true"></i> Редактировать</a>--></div>
                    <h3 class="panel-title">${item.task_title}</h3>
                  </div>
                  <div class="panel-body">
                    <#if item.hasPhoto>
                    <div class="pull-right img-thumbnail"><img src="${item.photo}"></img></div>
                    </#if>
                    ${item.task_description}
                  </div>
				  <div class="panel-footer mid">
				    <p class="pull-right mid">
				      <a href="/${item.cat_url}" class="mid">${item.cat_title}</a>
				    </p>
				    <b>Автор: </b>${item.user_name} ${item.user_phone}
				  </div>
                </div>
              <#else>
                  <h3 align="center"><b>Ничего не найдено.</b></h3>
              </#list>
              </div>
          </div>
        </div>
      </div>
      <!--/.container-->
      <!-- Bootstrap core JavaScript
         ================================================== -->
      <!-- Placed at the end of the document so the pages load faster -->
      <script src="./assets/jquery.min.js"></script>
      <script>window.jQuery || document.write('<script src="./assets/jquery.min.js"><\/script>')</script>
      <script src="./assets/bootstrap.min.js"></script>
      <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
      <script src="./assets/ie10-viewport-bug-workaround.js"></script>
      <script src="./assets/offcanvas.js"></script>
   </body>
</html>