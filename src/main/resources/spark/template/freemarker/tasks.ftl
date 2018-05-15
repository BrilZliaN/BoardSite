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
      <link href="/assets/bootstrap.min.css" rel="stylesheet">
      <link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">
      <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
      <link href="/assets/ie10-viewport-bug-workaround.css" rel="stylesheet">
      <!-- Custom styles for this template -->
      <link href="https://fonts.googleapis.com/css?family=PT+Sans" rel="stylesheet">
      <link href="/assets/offcanvas.css" rel="stylesheet">
      <link href="/assets/style.css" rel="stylesheet">
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
         <div class="row row-offcanvas row-offcanvas-right">
            <div class="col-xs-6 col-sm-3 sidebar-offcanvas" id="sidebar">
               <div class="list-group">
                  <#list projectcategories as item>
                  <a href="/${item.url}" class="list-group-item <#if item.url == pageurl>active</#if>">${item.title}</a>
                  </#list>
               </div>
            </div>
            <!--/.sidebar-offcanvas-->
            <div class="col-xs-12 col-sm-9">
               <p class="pull-right visible-xs">
                  <button type="button" class="btn btn-primary btn-xs" data-toggle="offcanvas">Типы услуг</button>
               </p>
              <h2>${pagetitle}</h2>
              <hr>
			  <#list tasks as item>
                <div class="panel panel-default">
                  <div class="panel-heading">
				    <div class="pull-right"><span class="label label-success"><b>${item.task_budget}</b></span></div>
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
				    <a href="/book/${item.task_id}" class="btn btn-default btn-sm mid">Заказать</a>
				  </div>
                </div>
              <#else>
                  <h3 align="center"><b>Ничего не найдено.</b></h3>
              </#list>
            </div>
         </div>
         <!--/row-->
         <hr>
         <footer class="mid">
			<p class="pull-right mid">
			  ${bottombuttons}
			</p>
            <p>© 2016 ${projectauthor}.</p>
         </footer>
      </div>
      <!--/.container-->
      <div class="modal fade" tabindex="-1" role="dialog" aria-labelledby="login" id="login">
         <div class="modal-dialog" role="document">
            <div class="modal-content">
               <div class="modal-header">
                  <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                  <h4 class="modal-title">Войдите на сайт</h4>
               </div>
               <div class="modal-body">
                  <form action="/login" method="post" class="form-horizontal">
                     <div class="form-group">
                        <label for="inputLogin" class="col-sm-2 control-label">Логин</label>
                        <div class="col-sm-10">
                           <input type="text" class="form-control" name="login" id="inputLogin" placeholder="Ваш логин">
                        </div>
                     </div>
                     <div class="form-group">
                        <label for="inputPassword" class="col-sm-2 control-label">Пароль</label>
                        <div class="col-sm-10">
                           <input type="password" class="form-control" name="password" id="inputPassword" placeholder="Ваш пароль">
                        </div>
                     </div>
                     <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-2">
                           <button type="submit" class="btn btn-primary">Войти</button>
                        </div>
                     </div>
                  </form>
                  <div class="modal-footer">
                     <a href="register">Еще не зарегистрированы?</a>
                  </div>
               </div>
            </div>
         </div>
      </div>
      <!-- Bootstrap core JavaScript
         ================================================== -->
      <!-- Placed at the end of the document so the pages load faster -->
      <script src="/assets/jquery.min.js"></script>
      <script>window.jQuery || document.write('<script src="/assets/jquery.min.js"><\/script>')</script>
      <script src="/assets/bootstrap.min.js"></script>
      <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
      <script src="/assets/ie10-viewport-bug-workaround.js"></script>
      <script src="/assets/offcanvas.js"></script>
   </body>
</html>