from django.urls import path
from .views import (get_user_info,
                    create_user_view,
                    authentication_view)



urlpatterns = [
    path('<username>/', get_user_info, name='detail'),
    path('create/new', create_user_view, name='create'),
    path('authenticate', authentication_view),
]
