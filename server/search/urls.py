from django.urls import path
from .views import search_profiles

urlpatterns = [
    path('', search_profiles)
    ]
