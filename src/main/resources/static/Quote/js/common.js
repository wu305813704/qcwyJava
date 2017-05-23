/**option
 * Created by Administrator on 2016/9/12.
 */

define(function(require, exports, module) {

	var common = {

		/**
		 * get请求
		 */
		get: function(url, parms, callback, error) {
			var that = this;

			$.ajax({
				type: 'GET',
				beforeSend: that.showloader(),
				url: baseURL + url,
				cache: false,
				data: parms,
				success: function(res) {

					console.info(res);

					res = JSON.parse(res);

					that.hideloader();

					if(typeof parms == "function") {
						parms(res);
					}

					if(typeof callback == 'function') {
						callback(res);
					}
				},
				error: function(res) {

					that.hideloader();

					console.log('get_err:' + res);

					if(typeof error == 'function') {
						error(res);
					}
				}
			});
		},

		/**
		 * post请求
		 * @param url
		 * @param parms
		 * @param callback
		 */
		post: function(url, parms, callback, error) {
			var that = this;
			$.ajax({
				type: 'POST',
				beforeSend: that.showloader(),
				url: baseURL + url,
				data: parms,
				success: function(res) {

					console.info(res);

					res = JSON.parse(res);

					that.hideloader();

					if(typeof callback == 'function') {
						callback(res);
					}
				},
				error: function(res) {

					that.hideloader();

					console.log('get_err:' + res);

					if(typeof error == 'function') {
						error(res);
					}
				}
			});
		},

		/**
		 * getJSON请求
		 * @param url
		 * @param parms
		 * @param callback
		 */
		getJSON: function(url, parms, success, warning, error) {
			$.getJSON(JAVAURL + url, parms, function(d) {
				try {
					if(d.result == true) {
						if(typeof success == 'function') {
							success(d);
						}
					} else {
						if(typeof warning == 'function') {
							warning(d);
							if(d.msg == '登录失效') {
								location.href = base + 'login.html';
							}
						}
					}
				} catch(e) {
					if(typeof error == 'function') {
						error(e);
					}
				}
			});
		},

		/**
		 * 弹出层组件
		 * @function
		 * msg  提示信息
		 */
		layer: {
			msg: function(msg) {
				if($('.layer_msg').length == 0) {
					$('body').append('<div class="layer_msg">' + msg + '</div>');
					setTimeout(function() {
						$('.layer_msg').remove();
					}, 2000);
				}
			},
			boxwithbtn: function(msg, okfn, calfn) {
				$('body').append('<div class="layer_bg"></div><div class="layer_box"><p>' + msg + '</p><a href="javascript:;" id="calbtn">取消</a><a href="javascript:;" id="okbtn">确定</a></div>');

				$('#calbtn').off('click').on('click', function() {
					if(typeof calfn == 'function') {
						calfn();
					}
					$('.layer_bg').remove();
					$('.layer_box').remove();
				});

				$('#okbtn').off('click').on('click', function() {
					if(typeof okfn == 'function') {
						okfn();
					}
					$('.layer_bg').remove();
					$('.layer_box').remove();
				});
			},
			showloading: function() {
				if($('.layer_loading').length == 0) {
					$('body').append('<div class="layer_loading"></div><div class="layer_load">加载中..</div>');
				}
			},
			hideloading: function() {
				$('.layer_load').remove();
				$('.layer_loading').remove();
			},
			divbox: function(content, id) {
				var html = [];

				if($('.layer_bg').length == 0) {
					$('body').append('<div class="layer_bg"></div>');
				}

				if(id && typeof id == 'string') {
					html.push('<div class="layer_divbox" id="' + id + '">')
				} else {
					html.push('<div class="layer_divbox">')
				}
				html.push('<a href="javascript:;" class="iconfont icon-close closebtn"></a>' + content + '</div>');

				$('.layer_bg').append(html.join(''));

				$('.layer_bg .closebtn').off('click').on('click', function() {
					$(this).parents('.layer_divbox').remove();

					if($('.layer_bg').html() == '') {
						$('.layer_bg').remove();
					}
				});
			},
			tip: function(event, hoverin, hoverout) {
				$(event).hover(function() {
					if($('.layer_tip').length == 0) {
						if(typeof hoverin == 'function') {
							hoverin(this);
						}
						if($(this).attr('tip') != '') {
							$(this).css('position', 'relative');
							$(this).append('<div class="layer_tip">' + $(this).attr('tip') + '</div>');
							$(this).find('.layer_tip').css({
								'margin-top': -($(this).find('.layer_tip').height() + 20) + 'px'
							});
						}
					}
				}, function() {
					$(this).css('position', 'inherit');
					if(typeof hoverout == 'function') {
						hoverout(this);
					}
					$('.layer_tip').remove();
				});
			}
		},

		/**
		 * 菜单处理
		 * @param pno
		 * @param cno
		 */
		menu: function(pno, cno, menus) {
			if(menus) {
				sessionStorage.setItem('menus', menus);
				return false;
			}

			$('.menu').html(sessionStorage.getItem('menus'));

			$('.menu>ul>li').off('click').on('click', function() {
				var $this = $(this);

				$('.menu>ul>li').removeClass('active');

				if(!$this.hasClass('active')) {
					$this.addClass('active');
				}

				if($this.find('a').length < 2) {
					$this.addClass('nomore');
				} else {
					$('.menu>ul>li').removeClass('nomore');
				}
				sessionStorage.setItem('menus', $(this).parents('.menu').html());
			});

			$('.menu>ul>li>ul>li>a').off('click').on('click', function() {
				$('.menu a').removeClass('active');
				$(this).addClass('active');
			});

		},

		/**
		 * 标签页处理
		 */
		tabcontr: function(callback) {
			$('.tabbox .tab').eq(0).addClass('active');
			$('.tabbox .tabcont').eq(0).addClass('active');

			$('.tabbox .tab').off('click').on('click', function() {
				var $index = $(this).index();
				$('.tabbox .tab').removeClass('active');
				$('.tabbox .tabcont').removeClass('active');
				$(this).addClass('active');
				$('.tabbox .tabcont').eq($index).addClass('active');

				if(callback && typeof callback == 'function') {
					callback()
				}
			});
		},

		/**
		 * 文件上传组件
		 */
		fileupload: {
			fileSelected: function(id) {
				var file = $(id).get(0).files[0];
				if(file) {
					//filetype 0:静图 1:动图 2:视频 3:其他
					if(file.type != '') {
						switch(file.type) {
							case 'image/jpeg':
								$('#filetype').val('0');
								common.fileupload.uploadFile(id, 0);
								break;
							case 'image/png':
								$('#filetype').val('0');
								common.fileupload.uploadFile(id, 0);
								break;
							case 'image/svg+xml':
								$('#filetype').val('0');
								common.fileupload.uploadFile(id, 0);
								break;
							case 'image/gif':
								$('#filetype').val('1');
								common.fileupload.uploadFile(id, 1);
								break;
							case 'video/mp4':
								$('#filetype').val('2');
								$('#progressNumber').show();
								$('#fileview').html('如果视频无法预览,因为您的浏览器过低或者不是MP4视频');
								common.fileupload.uploadFile(id, 2);
								break;
							case 'video/quicktime':
								$('#filetype').val('2');
								$('#progressNumber').show();
								$('#fileview').html('如果视频无法预览,因为您的浏览器过低或者不是MP4视频');
								common.fileupload.uploadFile(id, 2);
								break;
							case 'video/avi':
								$('#filetype').val('2');
								$('#progressNumber').show();
								$('#fileview').html('如果视频无法预览,因为您的浏览器过低或者不是MP4视频');
								common.fileupload.uploadFile(id, 2);
								break;
							case 'video/x-ms-wmv':
								$('#filetype').val('2');
								$('#progressNumber').show();
								$('#fileview').html('如果视频无法预览,因为您的浏览器过低或者不是MP4视频');
								common.fileupload.uploadFile(id, 2);
								break;
							case 'video/x-flv':
								$('#filetype').val('2');
								$('#progressNumber').show();
								$('#fileview').html('如果视频无法预览,因为您的浏览器过低或者不是MP4视频');
								common.fileupload.uploadFile(id, 2);
								break;
							case 'video/mpeg':
								$('#filetype').val('2');
								$('#progressNumber').show();
								$('#fileview').html('如果视频无法预览,因为您的浏览器过低或者不是MP4视频');
								common.fileupload.uploadFile(id, 2);
								break;
							case 'application/vnd.ms-excel':
								$('#filetype').val('5');
								common.fileupload.uploadFile(id, 5);
								break;
							default:
								common.layer.msg('请上传有效的图片或视频');
						}
					} else {
						$('#filetype').val('2');
						$('#progressNumber').show();
						$('#fileview').html('如果视频无法预览,因为您的浏览器过低或者不是MP4视频');
						common.fileupload.uploadFile(id, 2);
					}
				}
			},

			uploadFile: function(id, type) {
				var fd = new FormData();
				fd.append($(id).attr('name'), $(id).get(0).files[0]);
				var xhr = new XMLHttpRequest();
				xhr.upload.addEventListener("progress", common.fileupload.uploadProgress, false);
				xhr.addEventListener("load", common.fileupload.uploadComplete, false);
				xhr.addEventListener("error", common.fileupload.uploadFailed, false);
				xhr.addEventListener("abort", common.fileupload.uploadCanceled, false);
				if(type==5){
					xhr.open('POST', JAVAURL + '/teacher/studentexcerl2?callback=?&createuserid='+sessionStorage.getItem("createuserid")+'&filename=' + $(id).attr('name')); //修改成自己的接口
				}else{
					xhr.open('POST', NETURL + 'Ajax/AjaxUploadAdvFile.aspx?oper=uploadadvfile&filename=' + $(id).attr('name') +
					'&filetype=' + type); //修改成自己的接口
				}
				
				xhr.send(fd);
			},

			uploadProgress: function(evt) {
				if(evt.lengthComputable) {
					var percentComplete = Math.round(evt.loaded * 100 / evt.total);
					$('#progressNumber .progress').css('width', percentComplete.toString() + '%');
					if(percentComplete == 100) {
						$('#progressNumber').remove();
					}
				} else {
					common.layer.msg('上传进度异常');
				}
			},

			uploadComplete: function(evt) {
				/* 服务器端返回响应时候触发event事件*/
				var d = JSON.parse(evt.target.responseText);
				var fullname = [];
				var filename = [];
				var filetypes = ['mp4', 'flv', 'wmv', 'avi', 'mov', 'mpg'];
				if($.inArray(d.data.split('.')[1], filetypes) != -1) {
					common.videos('#fileview', NETURL + d.data);
				} else {
					$('#fileview').html('<img src="' + NETURL + d.data + '">');
				}

				$('#fileurl').val(d.data);
				for(var i in d.data.split('_')) {
					if(i > 0) {
						var item = d.data.split('_')[i];
						fullname.push(item);
					}
				}
				for(var f in fullname.join('_').split('.')) {
					if(f != (fullname.join('_').split('.').length - 1)) {
						var item = fullname.join('_').split('.')[f];
						filename.push(item);
					}
				}
				$('#filename').val(filename.join('.'));
				$('#filemd5').val(d.filemd5);
			},

			uploadFailed: function(evt) {
				common.layer.msg("上传失败");
			},

			uploadCanceled: function(evt) {
				common.layer.msg("取消上传");
			}
		},

		/**
		 * 播放器组件
		 */
		videos: function(id, url) {
			var html = '<video id="video_1" class="video-js vjs-default-skin vjs-big-play-centered" ' +
				'controls preload="none" data-setup="{}">' +
				'<source src="' + url + '" type="video/mp4"/>' +
				'<source src="' + url + '" type="video/webm"/>' +
				'<source src="' + url + '" type="video/ogg"/>' +
				'</video>';
			$(id).html(html);

			seajs.use(['plugin/video/video.min.js', 'plugin/video/video.min.css'], function() {
				videojs('video_1');
			});
		},

		/**
		 * 选择终端
		 */
		checkprint: function(isall, type) {
			//获取终端
			var GetPrint = function(d) {
				if(d.data.length == 0) {
					common.layer.msg('没有可用终端');
					return false;
				}
				if($('#printaddr').attr('pcids')) {
					var ids = $('#printaddr').attr('pcids').split(',');
				}
				if(type == 'view') {
					var html = '<div class="fullbox" id="fullbox"><ul id="city"></ul></div>';
				} else if(type == 'audit') {
					var html = '<div class="fullbox" id="fullbox"><ul id="city"></ul></div>';
				} else {
				
					var html = '<div class="fullbox" id="fullbox"><ul id="city"></ul></div>' +
						'<a href="javascript:;" id="SubCheck" class="btn">确定</a>';
				}
				common.layer.divbox(html);
				$('#fullbox').parents('.layer_divbox').css({
					'width': '400px',
					'height': '80%'
				});
				var chtml = [];
				var city_prints_list = [];
				for(var c = 0; c < d.data.length; c++) {
					var citem = d.data[c];
					var city_prints = 0;
					chtml.push('<li class="citys">');
					chtml.push('<a href="javascript:;">+</a>');
					chtml.push('<input type="checkbox" cid="' + citem.cityId + '" class="all"><p>' + citem.cityName + '</p>');
					chtml.push('<div><span class="checkes">0</span><p> / </p><span class="totals ctotal">0</span></div>');
					chtml.push('<div class="clearfix"></div>');

					//获取学校
					if(citem.schools && citem.schools.length > 0) {
						chtml.push('<ul>');
						for(var s = 0; s < citem.schools.length; s++) {
							var sitem = citem.schools[s];
							city_prints += sitem.printers.length;
							chtml.push('<li class="schools">');
							chtml.push('<a href="javascript:;">+</a>');
							chtml.push('<input type="checkbox" sid="' + sitem.schoolId + '" class="all"><p>' + sitem.schoolName + '</p>');
							chtml.push('<div><span class="checkes">0</span><p> / </p><span class="totals">' + sitem.printers.length + '</span></div>');
							chtml.push('<div class="clearfix"></div>');

							//获取终端
							if(sitem.printers && sitem.printers.length > 0) {
								chtml.push('<ul>');
								for(var p = 0; p < sitem.printers.length; p++) {
									var pitem = sitem.printers[p];
									chtml.push('<li class="printers">');
									if($('#printaddr').attr('pcids')) {
										if($.inArray(pitem.id.toString(), ids) != -1) {
											chtml.push('<input type="checkbox" class="view" pid="' + pitem.id + '" checked ' +
												'addr="' + citem.cityName + '-' + sitem.schoolName + '-' +
												pitem.printerAddInfo + '-' + pitem.printerName + '">');
										} else {
											chtml.push('<input type="checkbox" pid="' + pitem.id +
												'" addr="' + citem.cityName + '-' + sitem.schoolName + '-' +
												pitem.printerAddInfo + '-' + pitem.printerName + '">');
										}
									} else {
										chtml.push('<input type="checkbox" pid="' + pitem.id +
											'" addr="' + citem.cityName + '-' + sitem.schoolName + '-' +
											pitem.printerAddInfo + '-' + pitem.printerName + '">');
									}
									chtml.push('<p>' + pitem.printerAddInfo + '-' + pitem.printerName + '</p>');
									chtml.push('<div class="clearfix"></div>');
									chtml.push('</li>');
								}
								chtml.push('</ul>');
							}

							chtml.push('</li>');
						}
						chtml.push('</ul>');

						//记录计算总数
						city_prints_list.push(city_prints);
					}

					chtml.push('</li>');
				}
				$('#fullbox').html(chtml.join(''));
				if($('#printaddr').attr('pcids')) {
					for(var v = 0; v < $('.view').length; v++) {
						var $view = $('.view').eq(v);
						var $citys = $view.parents('.citys');
						var $schools = $view.parents('.schools');
						$citys.find('.checkes').eq(0).html($citys.find('.printers :checked').length);
						$schools.find('.checkes').eq(0).html($schools.find('.printers :checked').length);
					}
				}

				//输出计算总数
				for(var i = 0; i < city_prints_list.length; i++) {
					$('.citys').eq(i).find('.ctotal').html(city_prints_list[i]);
				}
				//选中事件
				$('#fullbox .printers :checkbox').off('click').on('click', function() {
					var $citys = $(this).parents('.citys');
					var $schools = $(this).parents('.schools');
					$citys.find('.checkes').eq(0).html($citys.find('.printers :checked').length);
					$schools.find('.checkes').eq(0).html($schools.find('.printers :checked').length);
					$citys.find(':checkbox').eq(0).prop('checked', false);
					$schools.find(':checkbox').eq(0).prop('checked', false);
				});

				//全选事件
				$('#fullbox .all').off('click').on('click', function() {
					var $li = $(this).parent('li');
					var printers = $li.find('ul :checkbox');
					if($(this).is(':checked')) {
						printers.prop('checked', true);
						if($li.hasClass('citys')) {
							for(var i = 0; i < $li.find('.schools').length; i++) {
								var $school = $li.find('.schools').eq(i);
								$school.find('.checkes').html($school.find('.totals').html());
							}
						} else {
							var $city = $li.parents('.citys');
							$city.find('.checkes').eq(0).html($city.find('.printers :checked').length);
						}
					} else {
						printers.prop('checked', false);
						if($li.hasClass('citys')) {
							$li.find('.schools .checkes').html(0);
						} else {
							var $city = $li.parents('.citys');
							$city.find('.checkes').eq(0).html($city.find('.printers :checked').length);
						}
					}
					$li.find('.checkes').eq(0).html($li.find('.printers :checked').length);
				});

				//展开事件
				$('#fullbox li a').off('click').on('click', function() {
					var $li = $(this).parent('li');
					if($(this).hasClass('open')) {
						$(this).removeClass('open').html('+');
						$li.find('ul').eq(0).slideUp();
					} else {
						$(this).addClass('open').html('-');
						$li.find('ul').eq(0).slideDown();
					}
				});

				//确定按钮事件
				$('#SubCheck').off('click').on('click', function() {
					var pcids = [];
					var addrs = [];
					for(var i = 0; i < $('.printers :checked').length; i++) {
						pcids.push($('.printers :checked').eq(i).attr('pid'));
						addrs.push($('.printers :checked').eq(i).attr('addr'));
					}
					$('#printaddr').attr('pcids', pcids.join(','));
					$('#printaddr').val(addrs.join(','));
					$('.layer_bg').remove();
				});
			};
			//选择终端点击事件
			$('#printaddr').off('click').on('click', function() {
				if(isall != true) {
					//添加广告
					if($('#uptime').val() == '') {
						common.layer.msg('请先选择上刊时间');
						return false;
					}
					if($('#downtime').val() == '') {
						common.layer.msg('请先选择下刊时间');
						return false;
					}
					var data = {};
					data.timespid = $('#interval :selected').val();
					data.downtime = $('#downtime').val();
					data.uptime = $('#uptime').val();
					data.isfilt = 0;
					common.getJSON('/admin/advsetplay/getprinteruser?callback=?', data, function(d) {
						//获取终端
						GetPrint(d);
					}, function(d) {
						$('.layer_bg').remove();
						if(d.msg) {
							common.layer.msg(d.msg);
						} else {
							common.layer.msg("数据异常");
						}
					}, function(e) {
						common.layer.msg("数据异常");
						console.log("CheckPrint,msg:" + e);
					});
				} else {
					//添加插播
					common.getJSON('/admin/advsetplay/getallPrinter?callback=?', {}, function(d) {
						//获取终端
						GetPrint(d);
					}, function(d) {
						$('.layer_bg').remove();
						if(d.msg) {
							common.layer.msg(d.msg);
						} else {
							common.layer.msg("数据异常");
						}
					}, function(e) {
						common.layer.msg("数据异常");
						console.log("CheckPrint,msg:" + e);
					});
				}
			});
		},

		/**
		 * 级联条件
		 */
		cascade: {
			//城市-学校-终端 下拉级联
			addrinfo: function(callback) {
				//获取城市列表
				common.getJSON('admin/adv/getcitylist?callback=?', {}, function(d) {
					var html = [];
					html.push('<option value="">全部</option>');
					for(var i in d.data) {
						var item = d.data[i];
						html.push('<option value="' + item.id + '">' + item.name + '</option>')
					}
					$('#city').html(html.join(''));

					if(typeof callback == 'function') {
						callback();
					}
				}, function(d) {
					if(d.msg) {
						common.layer.msg(d.msg);
					} else {
						common.layer.msg("数据异常");
					}
				}, function(e) {
					common.layer.msg("数据异常");
					console.log("CheckPrint,msg:" + e);
				});

				//选择城市，填充学校
				$('#city').off('change').on('change', function() {
					if($(this).val() == '') {
						$('#school').html('<option value="">全部</option>').attr('disabled', true);
					} else {
						$('#school').html('<option value="">全部</option>');
						common.getJSON('admin/adv/getschoollist?callback=?', {
							ids: $(this).val()
						}, function(d) {
							var html = [];
							html.push('<option value="">全部</option>');getJSON
							for(var i in d.data) {
								var item = d.data[i];
								html.push('<option value="' + item.id + '">' + item.name + '</option>')
							}
							$('#school').html(html.join('')).attr('disabled', false);
						}, function(d) {
							if(d.msg) {
								common.layer.msg(d.msg);
							} else {
								common.layer.msg("数据异常");
							}
						}, function(e) {
							common.layer.msg("数据异常");
							console.log("CheckPrint,msg:" + e);
						});
					}
				});

				//选择学校，填充终端
				$('#school').off('change').on('change', function() {
					if($(this).val() == '') {
						$('#print').html('<option value="">全部</option>').attr('disabled', true);
					} else {
						$('#print').html('<option value="">全部</option>');
						common.getJSON('admin/adv/getallprinter?callback=?', {
							schoolids: $(this).val()
						}, function(d) {
							var html = [];
							html.push('<option value="">全部</option>');
							for(var i in d.data) {
								var item = d.data[i];
								html.push('<option value="' + item.id + '">' + item.printerAddInfo + '-' + item.printerName + '</option>')
							}
							$('#print').html(html.join('')).attr('disabled', false);
						}, function(d) {
							if(d.msg) {
								common.layer.msg(d.msg);
							} else {
								common.layer.msg("数据异常");
							}
						}, function(e) {
							common.layer.msg("数据异常");
							console.log("CheckPrint,msg:" + e);
						});
					}
				});
			},

			//时段-P刊位 下拉级联
			timeinfo: function(callback) {
				common.getJSON('admin/advputset/getadvputsetlist?callback=?', {}, function(d) {
					var html = [];
					html.push('<option value="">全部</option>');
					for(var i in d.data) {
						var item = d.data[i];
						html.push('<option value="' + item.id + '" posNum="' + item.posNum + '">' + item.timesName + '</option>')
					}
					$('#module').html(html.join(''));

					//选择时段，填充刊位
					$('#module').off('change').on('change', function() {
						if($(this).val() == '') {
							$('#playaddr').html('<option value="">全部</option>').attr('disabled', true);
						} else {
							var html = [];
							html.push('<option value="">全部</option>');
							for(var i = 0; i < $(this).find(':selected').attr('posNum'); i++) {
								html.push('<option value="' + (i + 1) + '">P' + (i + 1) + '</option>')
							}
							$('#playaddr').html(html.join('')).attr('disabled', false);
						}
					});

					if(typeof callback == 'function') {
						callback();
					}
				}, function(d) {
					if(d.msg) {
						common.layer.msg(d.msg);
					} else {
						common.layer.msg("数据异常");
					}
				}, function(e) {
					common.layer.msg("数据异常");
					console.log("CheckPrint,msg:" + e);
				});
			}
		},

		/**
		 * 获取URL参数
		 */
		geturldata: function(name) {
			var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
			var r = window.location.search.substr(1).match(reg);
			if(r != null) return decodeURIComponent(r[2]);
			return null;
		},

		/**
		 * 时间戳转换日期
		 */
		unixtodate: function(unixTime) {
			var time = new Date(unixTime);
			var ymdhis = '';
			ymdhis += time.getFullYear() + '-';
			ymdhis += (time.getMonth() + 1) < 10 ? '0' + (time.getMonth() + 1) + '-' : (time.getMonth() + 1) + '-';
			ymdhis += time.getDate() < 10 ? '0' + time.getDate() + ' ' : time.getDate() + ' ';
			ymdhis += time.getHours() < 10 ? '0' + time.getHours() + ':' : time.getHours() + ':';
			ymdhis += time.getMinutes() < 10 ? '0' + time.getMinutes() + ':' : time.getMinutes() + ':';
			ymdhis += time.getSeconds() < 10 ? '0' + time.getSeconds() : time.getSeconds();
			return ymdhis;
		},

		/**
		 * 时间插件
		 */
		laydate: function(id, callback, hastime) {
			require('laydate');
			$(id).off('focus').on('focus', function() {

				if(hastime == true) {
					laydate({
						elem: id,
						event: 'focus', //响应事件。如果没有传入event，则按照默认的click
						istime: true, // 判断是否时间，如果true则判断时间合法性，乱输入的将提示然后清空
						format: 'YYYY-MM-DD hh:mm:ss', // 日期格式，hh:mm:ss则表示需要具体时间，另外，需要先选择时间，再选择日期，因为点击日期后将被认为是选择完毕。
						istoday: false, //是否显示今天的按钮，默认为显示
						isclear: true, //是否现在清空按钮
						issure: true, //是否显示确认按钮
						festival: false, //显示节日
						choose: callback
					});
					return false;
				}

				laydate({
					elem: id,
					event: 'focus', //响应事件。如果没有传入event，则按照默认的click
					istime: true, // 判断是否时间，如果true则判断时间合法性，乱输入的将提示然后清空
					format: 'YYYY-MM-DD', // 日期格式，hh:mm:ss则表示需要具体时间，另外，需要先选择时间，再选择日期，因为点击日期后将被认为是选择完毕。
					istoday: false, //是否显示今天的按钮，默认为显示
					isclear: true, //是否现在清空按钮
					issure: true, //是否显示确认按钮
					festival: false, //显示节日
					choose: callback
				});
			});
		},

		/**
		 * 关联年级班级
		 */
		Gradeclass:function(Gradeid,Classid,gid){
			//获取年级
			var createuserid = sessionStorage.getItem("createuserid");
			common.getJSON('/teacher/getradelist?callback=?', {
				createuserid: createuserid
			}, function(d) {
				var html = [];
				html.push('<option value="">全部</option>');
				for(var i in d.data) {
					var item = d.data[i];
					html.push('<option value="' + item.radeId + '">' + item.name + '</option>')
				}
				$('#'+Gradeid).html(html.join(''));
				if(gid!=undefined&&gid!=""){
					$('#'+Gradeid).val(gid);
				}
				
			}, function(d) {
				if(d.msg) {
					common.layer.msg(d.msg);
				} else {
					common.layer.msg("数据异常");
				}
			}, function(e) {
				common.layer.hideloading();
				common.layer.msg("数据异常");
				console.log("GetGrade,msg:" + e);
			});
			//选择城市，填充学校
			$('#'+Gradeid).off('change').on('change', function() {
				if($(this).val() == '') {
					$('#'+Classid).html('<option value="">全部</option>').attr('disabled', true);
				} else {
					$('#'+Classid).html('<option value="">全部</option>');
					common.getJSON('/teacher/getclasslist?callback=?', {
						radeid: $(this).val(),
						createuserid: createuserid
					}, function(d) {
						var html = [];
						html.push('<option value="">全部</option>');
						for(var i in d.data) {
							var item = d.data[i];
							html.push('<option value="' + item.classId + '">' + item.name + '</option>')
						}
						$('#'+Classid).html(html.join('')).attr('disabled', false);
					}, function(d) {
						if(d.msg) {
							common.layer.msg(d.msg);
						} else {
							common.layer.msg("数据异常");
						}
					}, function(e) {
						common.layer.msg("数据异常");
						console.log("CheckPrint,msg:" + e);
					});
				}
			});
		},
		
		/**
		 * 分页插件
		 */
		pager: function(url, data, index, size, callback, rows, id) {
			if(index == '' && size == '') {
				if(data.pageindex == 0) {
					index = 0;
				} else {
					index = data.pageindex - 1;
				}
				size = data.pagesize;
			} else {
				data.pageindex = index;
				data.pagesize = size;
			}
			if(!id) {
				id = '#tbody';
			}
			$('#pagination').remove();
			common.layer.showloading();
			common.getJSON(url, data, function(d) {
				if(d.data.total == 0) {
					$(id).html('<tr><td colspan="' + rows + '">暂无数据</td></tr>');
					common.layer.hideloading();
					return false;
				}
				if(d.data.datas.length > 0) {
					if(d.data.total > size) {
						require('pagination');
						require('paginationcss');
						$('.content').append('<div id="pagination"></div>');
						var PageCallback = function(pageindex) {
							if(pageindex != index) {
								index = pageindex;
								data.pageindex = index + 1;
								common.layer.showloading();
								common.getJSON(url, data, function(d) {
									if(d.data.datas.length > 0) {
										if(typeof callback == 'function') {
											callback(d);
										}
									} else {
										$(id).html('<tr><td colspan="' + rows + '">暂无数据</td></tr>');
									}
									common.layer.hideloading();
								}, function(d) {
									common.layer.hideloading();
									if(d.msg) {
										common.layer.msg(d.msg);
									} else {
										common.layer.msg("数据异常");
									}
								}, function(e) {
									common.layer.hideloading();
									common.layer.msg("数据异常");
									console.log("PageCallback,msg:" + e);
								});
							}
						};

						if(typeof callback == 'function') {
							callback(d);
						}

						if(localStorage.getItem('advdata')) {
							localStorage.clear();
						}
						$("#pagination").pagination(d.data.total, {
							num_edge_entries: 1, //边缘页数
							num_display_entries: 4, //主体页数
							items_per_page: size, //每页显示项
							current_page: index, //当前选中的页面,可选参数，默认是0，表示第1页
							callback: PageCallback
						});
					} else {
						if(typeof callback == 'function') {
							callback(d);
						}
					}
				} else {
					$(id).html('<tr><td colspan="' + rows + '">暂无数据</td></tr>');
				}
				common.layer.hideloading();
			}, function(d) {
				common.layer.hideloading();
				if(d.msg) {
					common.layer.msg(d.msg);
				} else {
					common.layer.msg("数据异常");
				}
				$(id).html('<tr><td colspan="' + rows + '">暂无数据</td></tr>');
			}, function(e) {
				common.layer.hideloading();
				common.layer.msg("数据异常");
				console.log("DataInit,msg:" + e);
			});
		}

	};






	module.exports = common;

});