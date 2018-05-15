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
                  <li><a href="/login">Вход</a></li>
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
            <h2>${pagetitle}</h2>
            <hr>
			<#if success>
			<div class="alert alert-success" role="alert"><i class="fa fa-check"></i> <b>Успех! </b> Ваши данные были изменены.</div>
			<#else>
            <#if error><div class="alert alert-danger" role="alert"><i class="fa fa-times"></i> <b>Ошибка!</b> Вы оставили некоторые поля пустыми.</div></#if>
            <div>
               <div class="modal-body">
                  <form action="/profile" method="post" class="form-horizontal">
                     <div class="form-group">
                        <label for="inputSurname" class="col-sm-2 control-label">Фамилия</label>
                        <div class="col-sm-10">
                           <input type="text" class="form-control" id="inputSurname" name="surname" value="${user.surname}" placeholder="Ваша фамилия">
                        </div>
                     </div>
                     <div class="form-group">
                        <label for="inputName" class="col-sm-2 control-label">Имя</label>
                        <div class="col-sm-10">
                           <input type="text" class="form-control" id="inputName" name="fullname" value="${user.name}" placeholder="Ваше полное имя">
                        </div>
                     </div>
                     <div class="form-group">
                        <label for="inputPatronymic" class="col-sm-2 control-label">Отчество</label>
                        <div class="col-sm-10">
                           <input type="text" class="form-control" id="inputPatronymic" name="patronymic" value="${user.patronymic}" placeholder="Ваше отчество">
                        </div>
                     </div>
                     <div class="form-group">
                        <label for="inputPhone" class="col-sm-2 control-label">Контактный телефон</label>
                        <div class="col-sm-10">
                           <input type="tel" class="form-control" id="inputPhone" name="phone" value="${user.phone}" placeholder="Ваш номер мобильного телефона в формате +79123456789">
                        </div>
                     </div>
                     <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-2">
                           <button type="submit" class="btn btn-primary">Изменить данные</button>
                        </div>
                     </div>
                  </form>
               </div>
            </div>
			</#if>
            </div>
			<!--/.col-xs-12.col-sm-9-->
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